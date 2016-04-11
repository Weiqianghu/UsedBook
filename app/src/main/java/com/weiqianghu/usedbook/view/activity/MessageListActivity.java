package com.weiqianghu.usedbook.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.entity.FailureMessageModel;
import com.weiqianghu.usedbook.model.entity.MessageModel;
import com.weiqianghu.usedbook.model.entity.UserBean;
import com.weiqianghu.usedbook.presenter.adapter.MessageAdapter;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.util.DateUtil;
import com.weiqianghu.usedbook.view.view.IRecycleViewItemClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.ObseverListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.GetListener;

public class MessageListActivity extends Activity implements ObseverListener, IRecycleViewItemClickListener {

    private TextView mTvTopBarText;
    private ImageView mIvTopBarLeftBtn;

    private List<MessageModel> mData = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwiperefreshLayout;
    private MessageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        initBmobIm();
        initView();
        initTopBar();
    }

    private void initData() {
        mData.clear();
        List<BmobIMConversation> conversationList = BmobIM.getInstance().loadAllConversation();
        for (int i = 0, length = conversationList.size(); i < length; i++) {

            final BmobIMConversation conversation = conversationList.get(i);
            final MessageModel model = new MessageModel();

            List<BmobIMMessage> msgs = conversation.getMessages();
            if (msgs != null && msgs.size() > 0) {
                BmobIMMessage lastMsg = msgs.get(0);
                String content = lastMsg.getContent();
                if (lastMsg.getMsgType().equals(BmobIMMessageType.TEXT.getType())) {
                    model.setMessageContent(content);
                } else if (lastMsg.getMsgType().equals(BmobIMMessageType.IMAGE.getType())) {
                    model.setMessageContent("[图片]");
                }
                model.setDate(DateUtil.getChatTime(lastMsg.getCreateTime()));
                final long unread = BmobIM.getInstance().getUnReadCount(conversation.getConversationId());
                model.setUnRead(unread);

                BmobQuery<UserBean> query = new BmobQuery<>();
                query.include("shop");
                query.getObject(MessageListActivity.this, conversation.getConversationId(), new GetListener<UserBean>() {
                    @Override
                    public void onSuccess(UserBean userBean) {
                        model.setChatUser(userBean);
                        if (userBean.getShop() != null) {
                            model.setTitle(userBean.getShop().getShopName());
                        } else {
                            model.setTitle(userBean.getUsername());
                        }
                        model.setImg(userBean.getImg());
                        model.setConversation(conversation);
                        mData.add(model);
                        mAdapter.notifyDataSetChanged();
                        mSwiperefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        FailureMessageModel failureMessageModel = new FailureMessageModel(i, s);
                        Toast.makeText(MessageListActivity.this, failureMessageModel.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void initTopBar() {
        mTvTopBarText = (TextView) findViewById(R.id.tv_top_bar_text);
        mTvTopBarText.setText(R.string.conversaction);

        Click click = new Click();
        mIvTopBarLeftBtn = (ImageView) findViewById(R.id.top_bar_left_button);
        mIvTopBarLeftBtn.setImageResource(R.mipmap.back);
        mIvTopBarLeftBtn.setOnClickListener(click);
    }

    private void initView() {

        mSwiperefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshlayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MessageAdapter(mData, R.layout.item_message);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mSwiperefreshLayout.setColorSchemeResources(R.color.mainColor);
        mSwiperefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }


    private void initBmobIm() {
        UserBean user = BmobUser.getCurrentUser(MessageListActivity.this, UserBean.class);
        BmobIM.connect(user.getObjectId(), new ConnectListener() {
            @Override
            public void done(String uid, BmobException e) {
                if (e == null) {
                } else {
                    Log.e("im", e.getErrorCode() + "/" + e.getMessage());
                }
            }
        });
    }

    class Click implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.top_bar_left_button:
                    finish();
                    break;
            }
        }
    }

    @Subscribe
    public void onEventMainThread(MessageEvent event) {
        //重新获取本地消息并刷新列表
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSwiperefreshLayout.setRefreshing(true);
        initData();
        BmobNotificationManager.getInstance(this).addObserver(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onPause() {
        //取消通知栏监听
        BmobNotificationManager.getInstance(this).removeObserver(this);
        super.onPause();
    }

    @Override
    public void onItemClick(View view, int position) {
        final UserBean user = mData.get(position).getChatUser();

        BmobIMUserInfo info = new BmobIMUserInfo();
        info.setUserId(user.getObjectId());
        info.setName(user.getUsername());
        info.setAvatar(user.getImg());

        BmobIM.getInstance().startPrivateConversation(info, new ConversationListener() {
            @Override
            public void done(BmobIMConversation c, BmobException e) {
                if (e == null) {
                    Intent intent = new Intent(MessageListActivity.this, ChatActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("c", c);
                    bundle.putParcelable(Constant.DATA, user);
                    intent.putExtra("c", bundle);

                    startActivity(intent);
                } else {
                    Toast.makeText(MessageListActivity.this, e.getMessage() + "(" + e.getErrorCode() + ")", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    ItemTouchHelper.Callback mCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
        /**
         * @param recyclerView
         * @param viewHolder 拖动的ViewHolder
         * @param target 目标位置的ViewHolder
         * @return
         */
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            
            BmobIM.getInstance().deleteConversation(mData.get(position).getConversation());

            mData.remove(position);
            mAdapter.notifyItemRemoved(position);
        }
    };
}
