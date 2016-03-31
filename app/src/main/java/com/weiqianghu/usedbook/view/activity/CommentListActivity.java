package com.weiqianghu.usedbook.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.entity.BookBean;
import com.weiqianghu.usedbook.model.entity.CommentBean;
import com.weiqianghu.usedbook.model.entity.OrderBean;
import com.weiqianghu.usedbook.presenter.QueryCommentPresenter;
import com.weiqianghu.usedbook.presenter.adapter.CommentAdapter;
import com.weiqianghu.usedbook.presenter.adapter.OrderAdapter;
import com.weiqianghu.usedbook.util.CallBackHandler;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.view.customview.EmptyRecyclerView;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentListActivity extends AppCompatActivity {

    private TextView mTvTopBarText;
    private ImageView mIvTopBarLeftBtn;

    private List<CommentBean> mData = new ArrayList<>();
    private CommentAdapter mCommentAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private EmptyRecyclerView mRecyclerview;
    private SwipeRefreshLayout mSwiperefreshlayout;

    private QueryCommentPresenter mQueryCommentPresenter;

    private boolean isRefresh = false;
    private int count = 0;
    private static final int STEP = 15;

    private BookBean mBook = new BookBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);

        initTopBar();
        initView();
        initData();
    }

    private void initView() {
        this.mSwiperefreshlayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshlayout);
        this.mRecyclerview = (EmptyRecyclerView) findViewById(R.id.recyclerview);

        View empty = findViewById(R.id.book_empty);
        TextView emptyTv = (TextView) findViewById(R.id.tv_empty);
        emptyTv.setText(R.string.comment_empty);
        mRecyclerview.setEmptyView(empty);
        mLayoutManager = new LinearLayoutManager(this);
        mCommentAdapter = new CommentAdapter(mData, R.layout.item_comment, this);

        mRecyclerview.setLayoutManager(mLayoutManager);
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setAdapter(mCommentAdapter);
        mRecyclerview.setItemAnimator(new DefaultItemAnimator());
        mRecyclerview.setOnScrollListener(onScrollListener);

        mSwiperefreshlayout.setColorSchemeResources(R.color.mainColor);
        mSwiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                count = 0;
                isRefresh = true;
                queryData(count * STEP, STEP);
            }
        });

        mQueryCommentPresenter = new QueryCommentPresenter(queryCommentHandler);

    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getBundleExtra(Constant.DATA);
            if (bundle != null) {
                mBook = bundle.getParcelable(Constant.DATA);
                mBook.setObjectId(mBook.getObjectIdStr());
            }
        }
        isRefresh = true;
        count = 0;
        mSwiperefreshlayout.post(new Runnable() {
            @Override
            public void run() {
                mSwiperefreshlayout.setRefreshing(true);
                queryData(count * STEP, STEP);
            }
        });

    }

    private void queryData(int start, int step) {
        mQueryCommentPresenter.queryComment(CommentListActivity.this, start, step, mBook);
    }

    private void initTopBar() {
        mTvTopBarText = (TextView) findViewById(R.id.tv_top_bar_text);
        mTvTopBarText.setText(R.string.publish_comment);

        Click click = new Click();
        mIvTopBarLeftBtn = (ImageView) findViewById(R.id.top_bar_left_button);
        mIvTopBarLeftBtn.setImageResource(R.mipmap.back);
        mIvTopBarLeftBtn.setOnClickListener(click);
    }


    private class Click implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.top_bar_left_button:
                    onBackPressed();
                    break;
            }
        }
    }

    CallBackHandler queryCommentHandler = new CallBackHandler() {
        @Override
        public void handleSuccessMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCESS:
                    Bundle bundle = msg.getData();
                    if (isRefresh) {
                        mData.clear();
                        isRefresh = false;
                    }
                    List<CommentBean> list = bundle.getParcelableArrayList(Constant.LIST);
                    mData.addAll(list);
                    mCommentAdapter.notifyDataSetChanged();
                    mSwiperefreshlayout.setRefreshing(false);
            }

        }

        @Override
        public void handleFailureMessage(String msg) {
            Toast.makeText(CommentListActivity.this, msg, Toast.LENGTH_SHORT).show();
            mSwiperefreshlayout.setRefreshing(false);
        }
    };

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
}
