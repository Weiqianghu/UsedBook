package com.weiqianghu.usedbook.view.activity;

import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.entity.BookBean;
import com.weiqianghu.usedbook.model.entity.BookModel;
import com.weiqianghu.usedbook.model.entity.PreferBean;
import com.weiqianghu.usedbook.model.entity.RecommendBean;
import com.weiqianghu.usedbook.model.entity.UserBean;
import com.weiqianghu.usedbook.presenter.QueryBookImgsPresenter;
import com.weiqianghu.usedbook.presenter.QueryBooksPresenter;
import com.weiqianghu.usedbook.presenter.QueryRecommendPresenter;
import com.weiqianghu.usedbook.presenter.UpdatePresenter;
import com.weiqianghu.usedbook.presenter.adapter.BookAdapter;
import com.weiqianghu.usedbook.presenter.adapter.PreferAdapter;
import com.weiqianghu.usedbook.presenter.adapter.RecommendAdapter;
import com.weiqianghu.usedbook.util.CallBackHandler;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.util.FragmentUtil;
import com.weiqianghu.usedbook.view.customview.EmptyRecyclerView;
import com.weiqianghu.usedbook.view.fragment.BookDetailFragment;
import com.weiqianghu.usedbook.view.view.IRecycleViewItemClickListener;
import com.weiqianghu.usedbook.view.view.IUpdateView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

public class RecommendActivity extends AppCompatActivity implements IRecycleViewItemClickListener, IUpdateView {
    private TextView mTvTopBarText;
    private ImageView mIvTopBarLeftBtn;

    private EmptyRecyclerView mRecyclerView;
    private List<BookModel> mData = new ArrayList();
    private List<RecommendBean> mRecommends = new ArrayList<>();
    private RecommendAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private QueryRecommendPresenter mQueryRecommendPresenter;
    private QueryBookImgsPresenter mQueryBookImgsPresenter;
    private boolean isRefresh = false;
    private int count = 0;
    private static final int STEP = 15;

    private UserBean mUser = new UserBean();
    private FragmentManager mFragmentManager;

    private View mContainer;
    private View mMain;

    private UpdatePresenter<RecommendBean> mUpdatePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        initTopBar();
        initView();
        initData();
    }

    private void initView() {
        mContainer = findViewById(R.id.main_container);
        mMain = findViewById(R.id.top_bar);
        mRecyclerView = (EmptyRecyclerView) findViewById(R.id.recyclerview);
        View empty = findViewById(R.id.empty);
        mRecyclerView.setEmptyView(empty);
        TextView emptyTv = (TextView) findViewById(R.id.tv_empty);
        emptyTv.setText(R.string.recommend_empty);
        mLayoutManager = new LinearLayoutManager(RecommendActivity.this);
        mAdapter = new RecommendAdapter(mData, R.layout.item_recommend);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setOnScrollListener(onScrollListener);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.mainColor);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                count = 0;
                isRefresh = true;
                queryData(count * STEP, STEP);
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        mQueryRecommendPresenter = new QueryRecommendPresenter(queryRecommendHandler);
        mQueryBookImgsPresenter = new QueryBookImgsPresenter(queryBookImgsHandler);
        mUpdatePresenter = new UpdatePresenter<RecommendBean>(this, updateHandler);
    }

    private void initData() {
        mUser = BmobUser.getCurrentUser(RecommendActivity.this, UserBean.class);

        isRefresh = true;
        count = 0;
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                queryData(count * STEP, STEP);
            }
        });

    }

    private void queryData(int start, int step) {
        mQueryRecommendPresenter.queryRecommends(RecommendActivity.this, mUser, start, step);
    }

    private void initTopBar() {
        mTvTopBarText = (TextView) findViewById(R.id.tv_top_bar_text);
        mTvTopBarText.setText(R.string.recomend);

        mIvTopBarLeftBtn = (ImageView) findViewById(R.id.top_bar_left_button);
        mIvTopBarLeftBtn.setImageResource(R.mipmap.back);
        mIvTopBarLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        gotoBookDetail(position);
    }

    CallBackHandler queryRecommendHandler = new CallBackHandler() {
        @Override
        public void handleSuccessMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCESS:
                    Bundle bundle = msg.getData();
                    List list = bundle.getParcelableArrayList(Constant.LIST);
                    if (list != null && list.size() > 0) {
                        mRecommends.clear();
                        mRecommends.addAll(list);
                        for (int i = 0, length = mRecommends.size(); i < length; i++) {
                            mQueryBookImgsPresenter.queryBookImgs(RecommendActivity.this, mRecommends.get(i).getBook());
                        }
                    } else {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
            }
        }

        @Override
        public void handleFailureMessage(String msg) {
            Toast.makeText(RecommendActivity.this, msg, Toast.LENGTH_SHORT).show();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };

    CallBackHandler queryBookImgsHandler = new CallBackHandler() {
        @Override
        public void handleSuccessMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCESS:
                    Bundle bundle = msg.getData();
                    List list = bundle.getParcelableArrayList(Constant.LIST);
                    BookBean bookBean = bundle.getParcelable(Constant.BOOK);

                    BookModel bookModel = new BookModel();
                    bookModel.setBook(bookBean);
                    bookModel.setBookImgs(list);

                    if (isRefresh) {
                        mData.clear();
                        isRefresh = false;
                    }
                    mData.add(bookModel);

                    mAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
            }
        }

        @Override
        public void handleFailureMessage(String msg) {
            Toast.makeText(RecommendActivity.this, msg, Toast.LENGTH_SHORT).show();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };

    void gotoBookDetail(int position) {

        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }
        Fragment fragment = mFragmentManager.findFragmentByTag(BookDetailFragment.TAG);
        if (fragment == null) {
            fragment = BookDetailFragment.getInstance();

            BookModel bookModel = new BookModel();
            bookModel.setBook(mData.get(position).getBook());
            bookModel.setBookImgs(mData.get(position).getBookImgs());

            Bundle args = new Bundle();
            args.putParcelable(Constant.BOOK, bookModel);
            fragment.setArguments(args);
        } else {
            Bundle args = fragment.getArguments();

            BookModel bookModel = new BookModel();
            bookModel.setBook(mData.get(position).getBook());
            bookModel.setBookImgs(mData.get(position).getBookImgs());

            args.putParcelable(Constant.BOOK, bookModel);
        }
        FragmentUtil.addContentNoAnim(R.id.main_container, fragment, mFragmentManager, BookDetailFragment.TAG);
        mContainer.setVisibility(View.VISIBLE);
    }

    private void loadMore() {
        count++;
        queryData(count * STEP, STEP);
    }

    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {

        private int totalItemCount;
        private int lastVisibleItem;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if (lastVisibleItem >= totalItemCount - 1) {
                loadMore();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
            totalItemCount = mLayoutManager.getItemCount();
        }
    };

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

            RecommendBean recommend = mRecommends.get(position);
            recommend.setRead(true);

            mUpdatePresenter.update(RecommendActivity.this, recommend, recommend.getObjectId());

            mRecommends.remove(position);
            mAdapter.notifyItemRemoved(position);
        }
    };

    CallBackHandler updateHandler = new CallBackHandler() {
        @Override
        public void handleFailureMessage(String msg) {
            Toast.makeText(RecommendActivity.this, msg, Toast.LENGTH_SHORT).show();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };


}
