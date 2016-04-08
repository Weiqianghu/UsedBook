package com.weiqianghu.usedbook.view.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.entity.BookBean;
import com.weiqianghu.usedbook.model.entity.BookModel;
import com.weiqianghu.usedbook.presenter.QueryBookImgsPresenter;
import com.weiqianghu.usedbook.presenter.QueryBooksPresenter;
import com.weiqianghu.usedbook.presenter.adapter.BookAdapter;
import com.weiqianghu.usedbook.util.CallBackHandler;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.util.FragmentUtil;
import com.weiqianghu.usedbook.view.common.BaseFragment;
import com.weiqianghu.usedbook.view.customview.EmptyRecyclerView;
import com.weiqianghu.usedbook.view.view.IRecycleViewItemClickListener;

import java.util.ArrayList;
import java.util.List;


public class SearchResultFragment extends BaseFragment implements IRecycleViewItemClickListener {

    public static final String TAG = SearchResultFragment.class.getSimpleName();
    private TextView mTvTopBarText;
    private ImageView mIvTopBarLeftBtn;

    private TextView mEmptyTv;

    private String mSearchPar = "";
    private QueryBooksPresenter mQueryBooksPresenter;

    private EmptyRecyclerView mRecyclerView;
    private List<BookModel> mData = new ArrayList();
    private List<BookBean> mBooks = new ArrayList<>();
    private BookAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private QueryBookImgsPresenter mQueryBookImgsPresenter;
    private boolean isRefresh = false;
    private int count = 0;
    private static final int STEP = 15;
    private FragmentManager mFragmentManager;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_result;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView(savedInstanceState);
    }

    private void initTopBar() {
        mTvTopBarText = (TextView) mRootView.findViewById(R.id.tv_top_bar_text);
        mTvTopBarText.setText(mSearchPar);

        Click click = new Click();
        mIvTopBarLeftBtn = (ImageView) mRootView.findViewById(R.id.top_bar_left_button);
        mIvTopBarLeftBtn.setImageResource(R.mipmap.back);
        mIvTopBarLeftBtn.setOnClickListener(click);
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mSearchPar = bundle.getString(Constant.DATA);
        }
        initTopBar();
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
        mQueryBooksPresenter.queryBooks(getActivity(), mSearchPar, start, step);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mRecyclerView = (EmptyRecyclerView) mRootView.findViewById(R.id.recyclerview);
        View empty = mRootView.findViewById(R.id.book_empty);
        mEmptyTv= (TextView) mRootView.findViewById(R.id.tv_empty);
        mEmptyTv.setText(R.string.search_result_empty);
        mRecyclerView.setEmptyView(empty);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new BookAdapter(mData, R.layout.item_book);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setOnScrollListener(onScrollListener);

        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.mainColor);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                count = 0;
                isRefresh = true;
                queryData(count * STEP, STEP);
            }
        });

        mQueryBooksPresenter = new QueryBooksPresenter(queryBooksHandler);
        mQueryBookImgsPresenter = new QueryBookImgsPresenter(queryBookImgsHandler);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        }, 500);
    }

    CallBackHandler queryBooksHandler = new CallBackHandler() {
        @Override
        public void handleSuccessMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCESS:
                    Bundle bundle = msg.getData();
                    List list = bundle.getParcelableArrayList(Constant.LIST);
                    if (list != null && list.size() > 0) {
                        mBooks.clear();
                        mBooks.addAll(list);
                        for (int i = 0, length = mBooks.size(); i < length; i++) {
                            mQueryBookImgsPresenter.queryBookImgs(getActivity(), (BookBean) list.get(i));
                        }
                    } else {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
            }
        }

        @Override
        public void handleFailureMessage(String msg) {
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };

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

    private void loadMore() {
        count++;
        queryData(count * STEP, STEP);
    }

    @Override
    public void onItemClick(View view, int postion) {
        gotoBookDetail(postion);
    }

    private void gotoBookDetail(int position) {

        if (mFragmentManager == null) {
            mFragmentManager = getActivity().getSupportFragmentManager();
        }
        Fragment to = mFragmentManager.findFragmentByTag(BookDetailFragment.TAG);
        if (to == null) {
            to = new BookDetailFragment();
        }

        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.BOOK, mData.get(position));
        to.setArguments(bundle);

        Fragment from = mFragmentManager.findFragmentByTag(SearchResultFragment.TAG);
        FragmentUtil.switchContentAddToBackStack(from, to, R.id.main_container, mFragmentManager, BookDetailFragment.TAG);
    }

    class Click implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.top_bar_left_button:
                    getActivity().onBackPressed();
                    break;
            }
        }
    }
}
