package com.weiqianghu.usedbook.view.fragment;


import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.entity.BookBean;
import com.weiqianghu.usedbook.model.entity.BookModel;
import com.weiqianghu.usedbook.presenter.QueryBookImgsPresenter;
import com.weiqianghu.usedbook.presenter.QueryBooksPresenter;
import com.weiqianghu.usedbook.presenter.adapter.BooksAdapter;
import com.weiqianghu.usedbook.presenter.adapter.CommonAdapter;
import com.weiqianghu.usedbook.util.CallBackHandler;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.util.FragmentUtil;
import com.weiqianghu.usedbook.view.ViewHolder;
import com.weiqianghu.usedbook.view.common.BaseFragment;
import com.weiqianghu.usedbook.view.view.IBooksView;

import java.util.ArrayList;
import java.util.List;


public class MainFragment extends BaseFragment implements IBooksView {

    private GridView mBookGridView;
    private FragmentManager fragmentManager;
    private TextView mSearchEditText;
    private BooksAdapter mAdapter;

    private QueryBooksPresenter mQueryBooksPresenter;
    private QueryBookImgsPresenter mQueryBookImgsPresenter;
    private List<BookBean> mBooks = new ArrayList<>();
    private List<BookModel> mData = new ArrayList();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int count = 0;
    private static final int STEP = 30;
    private boolean isRefresh = false;

    @Override
    protected int getLayoutId() {
        Fresco.initialize(getActivity());
        return R.layout.fragment_main;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView(savedInstanceState);
        initData();
    }

    protected void initView(Bundle savedInstanceState) {
        mBookGridView = (GridView) mRootView.findViewById(R.id.gv_book);
        mAdapter = new BooksAdapter(getActivity(), mData, R.layout.book_item);
        mBookGridView.setAdapter(mAdapter);
        mBookGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gotoBookDetail(position);
            }
        });

        mBookGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    //滚动到底部
                    if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                        loadMore();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


        mSearchEditText = (TextView) mRootView.findViewById(R.id.et_search);
        mSearchEditText.setFocusable(false);
        mSearchEditText.setOnClickListener(new ClickListener());

        mQueryBooksPresenter = new QueryBooksPresenter(queryBooksHandler);
        mQueryBookImgsPresenter = new QueryBookImgsPresenter(queryBookImgsHandler);

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

    }

    private void initData() {
        count = 0;
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                isRefresh = true;
                mSwipeRefreshLayout.setRefreshing(true);
                queryData(count * STEP, STEP);
            }
        });

    }

    private void queryData(int start, int step) {
        mQueryBooksPresenter.queryBooks(getActivity(), start, step);
    }

    private void loadMore() {
        count++;
        queryData(count * STEP, STEP);
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

    void gotoBookDetail(int position) {

        if (fragmentManager == null) {
            fragmentManager = getActivity().getSupportFragmentManager();
        }
        Fragment fragment = fragmentManager.findFragmentByTag(BookDetailFragment.TAG);
        if (fragment == null) {
            fragment = BookDetailFragment.getInstance();

            Bundle args = new Bundle();
            args.putParcelable(Constant.BOOK, mData.get(position));
            fragment.setArguments(args);
        } else {
            Bundle args = fragment.getArguments();
            args.putParcelable(Constant.BOOK, mData.get(position));
        }

        Fragment form = fragmentManager.findFragmentByTag(MainLayoutFragment.TAG);

        FragmentUtil.switchContentAddToBackStack(form, fragment, R.id.main_container, fragmentManager, BookDetailFragment.TAG);
    }


    private class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.et_search:
                    gotoSearch();
            }

        }
    }

    void gotoSearch() {
        if (fragmentManager == null) {
            fragmentManager = getActivity().getSupportFragmentManager();
        }

        Fragment fragment = fragmentManager.findFragmentByTag(SearchFragment.TAG);
        if (fragment == null) {
            fragment = SearchFragment.getInstance();
        }

        Fragment from = fragmentManager.findFragmentByTag(MainLayoutFragment.TAG);
        FragmentUtil.switchContentAddToBackStack(from, fragment, R.id.main_container, fragmentManager, SearchFragment.TAG);
    }

}
