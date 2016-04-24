package com.weiqianghu.usedbook.view.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.entity.AssociateBean;
import com.weiqianghu.usedbook.model.entity.BookBean;
import com.weiqianghu.usedbook.model.entity.CommentBean;
import com.weiqianghu.usedbook.model.entity.RecommendBean;
import com.weiqianghu.usedbook.model.entity.UserBean;
import com.weiqianghu.usedbook.presenter.QueryCommentPresenter;
import com.weiqianghu.usedbook.presenter.SaveAssociatePresenter;
import com.weiqianghu.usedbook.presenter.SaveRecommendPresenter;
import com.weiqianghu.usedbook.util.AprioriUtil;
import com.weiqianghu.usedbook.util.CallBackHandler;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.util.ThreadPool;
import com.weiqianghu.usedbook.view.activity.MessageListActivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.WeakHashMap;

import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.BmobUser;

public class AprioriRecommendService extends Service {

    private ThreadLocal<QueryCommentPresenter> mQueryCommentsPresenter = new ThreadLocal<>();
    private UserBean mCurrentUser;
    private SaveRecommendPresenter mSaveRecommendPresenter;
    private SaveAssociatePresenter mSaveAssociatePresenter;

    private int count = 0;
    private static final int STEP = 30;

    Map<String, Set<String>> inputMap = new WeakHashMap<String, Set<String>>();
    private Set<String> mUserBooks = new TreeSet<>();

    public AprioriRecommendService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mCurrentUser = BmobUser.getCurrentUser(this, UserBean.class);
        if (mCurrentUser == null) {
            return;
        }
        mSaveRecommendPresenter = new SaveRecommendPresenter(RecommendBatchSaveHandler);
        mSaveAssociatePresenter = new SaveAssociatePresenter(AssociateBatchSaveHandler);
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        new RecommendAsync().executeOnExecutor(ThreadPool.getThreadPool());

        return super.onStartCommand(intent, flags, startId);
    }

    private void recommend() {
        mQueryCommentsPresenter.get().queryComment(AprioriRecommendService.this, STEP * count, STEP);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    CallBackHandler RecommendBatchSaveHandler = new CallBackHandler() {
        @Override
        public void handleSuccessMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCESS:
                    Context context = AprioriRecommendService.this;
                    Intent pendingIntent = new Intent(context, MessageListActivity.class);
                    Bitmap largetIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.recommend);
                    BmobNotificationManager.getInstance(context).showNotification(largetIcon, "小U为您推荐了图书", "为您推荐了图书,点击查看",
                            "为您推荐了图书,点击查看", pendingIntent);
            }
        }

        @Override
        public void handleFailureMessage(String msg) {
            Toast.makeText(AprioriRecommendService.this, msg, Toast.LENGTH_SHORT).show();
        }
    };

    CallBackHandler AssociateBatchSaveHandler = new CallBackHandler() {
        @Override
        public void handleSuccessMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCESS:
            }
        }

        @Override
        public void handleFailureMessage(String msg) {
            Toast.makeText(AprioriRecommendService.this, msg, Toast.LENGTH_SHORT).show();
        }
    };

    private class RecommendAsync extends AsyncTask<Void, Void, Void> {
        private int count = 0;

        @Override
        protected Void doInBackground(final Void... params) {
            Looper.prepare();

            CallBackHandler queryCommentsHandler = new CallBackHandler() {
                @Override
                public void handleSuccessMessage(final Message msg) {
                    switch (msg.what) {
                        case Constant.SUCCESS:
                            Bundle bundle = msg.getData();
                            List<CommentBean> comments = bundle.getParcelableArrayList(Constant.LIST);

                            for (CommentBean comment : comments) {
                                UserBean user = comment.getUser();
                                BookBean book = comment.getBook();

                                if (user != null && user.getObjectId() != null
                                        && book != null && book.getObjectId() != null) {
                                    String userObjectId = user.getObjectId();
                                    String bookObjectId = book.getObjectId();
                                    if (inputMap.get(userObjectId) == null) {
                                        Set<String> set = new TreeSet<>();
                                        set.add(bookObjectId);
                                        inputMap.put(userObjectId, set);
                                    } else {
                                        Set<String> set = inputMap.get(userObjectId);
                                        set.add(bookObjectId);
                                        inputMap.put(userObjectId, set);
                                    }
                                }
                            }

                            if (comments != null && comments.size() > 0) {
                                count++;
                                mQueryCommentsPresenter.get().queryComment(AprioriRecommendService.this, STEP * count, STEP);
                            } else {
                                List<Set<String>> trans = new LinkedList<Set<String>>();
                                for (Map.Entry<String, Set<String>> entry : inputMap.entrySet()) {
                                    trans.add(entry.getValue());
                                }
                                mUserBooks = inputMap.get(mCurrentUser.getObjectId());

                                Map<Map<Set<String>, Set<String>>, Double> results = AprioriUtil.aprioriRecommend(trans);

                                List<AssociateBean> associateBeanList = new ArrayList<>();
                                List<RecommendBean> recommendBeanList = new ArrayList<>();

                                for (Map.Entry<Map<Set<String>, Set<String>>, Double> result : results.entrySet()) {
                                    for (Map.Entry<Set<String>, Set<String>> map : result.getKey().entrySet()) {
                                        System.out.println(map.getKey() + "=>" + map.getValue() + ":" + result.getValue());

                                        AssociateBean associateBean = new AssociateBean();
                                        associateBean.setBaseBook(new ArrayList<>(map.getKey()));
                                        associateBean.setAssociateBook(new ArrayList<>(map.getValue()));
                                        associateBean.setConfidence(result.getValue());

                                        associateBeanList.add(associateBean);

                                        if (mUserBooks != null && mUserBooks.containsAll(associateBean.getBaseBook())) {

                                            Set<String> books = new TreeSet<>(associateBean.getAssociateBook());

                                            for (String bookObjectId : books) {
                                                if (mUserBooks.contains(bookObjectId)) {
                                                    continue;
                                                }

                                                BookBean bookBean = new BookBean();
                                                bookBean.setObjectId(bookObjectId);

                                                RecommendBean recommendBean = new RecommendBean();
                                                recommendBean.setBook(bookBean);
                                                recommendBean.setUser(mCurrentUser);
                                                recommendBean.setRead(false);

                                                recommendBeanList.add(recommendBean);
                                            }

                                        }
                                    }
                                    if (associateBeanList.size() >= 30) {
                                        mSaveAssociatePresenter.batchSaveAssociate(AprioriRecommendService.this, associateBeanList);
                                        associateBeanList.clear();

                                        mSaveRecommendPresenter.batchSaveRecommend(AprioriRecommendService.this, recommendBeanList);
                                        recommendBeanList.clear();


                                    }
                                }
                                mSaveAssociatePresenter.batchSaveAssociate(AprioriRecommendService.this, associateBeanList);
                                mSaveRecommendPresenter.batchSaveRecommend(AprioriRecommendService.this, recommendBeanList);
                            }
                    }
                }

                @Override
                public void handleFailureMessage(String msg) {
                    Toast.makeText(AprioriRecommendService.this, msg, Toast.LENGTH_SHORT).show();
                }
            };

            mQueryCommentsPresenter.set(new QueryCommentPresenter(queryCommentsHandler));
            recommend();
            Looper.loop();
            return null;
        }
    }
}
