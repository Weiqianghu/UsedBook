package com.weiqianghu.usedbook.view.fragment;


import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.entity.BookModel;
import com.weiqianghu.usedbook.model.entity.PreferBean;
import com.weiqianghu.usedbook.model.entity.PreferModel;
import com.weiqianghu.usedbook.model.entity.UserBean;
import com.weiqianghu.usedbook.presenter.QueryBookImgsPresenter;
import com.weiqianghu.usedbook.presenter.QueryPreferPresenter;
import com.weiqianghu.usedbook.presenter.adapter.PreferAdapter;
import com.weiqianghu.usedbook.util.CallBackHandler;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.util.FragmentUtil;
import com.weiqianghu.usedbook.view.common.BaseFragment;
import com.weiqianghu.usedbook.view.view.IRecycleViewItemClickListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;


public class PreferFragment extends BaseFragment implements IRecycleViewItemClickListener {

    public static final String TAG = PreferFragment.class.getSimpleName();

    private TextView mTvTopBarText;
    private ImageView mIvTopBarLeftBtn;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int count = 0;
    private static final int STEP = 5;
    private boolean isRefresh = false;
    private QueryBookImgsPresenter mQueryBookImgsPresenter;
    private QueryPreferPresenter mQueryPreferPresenter;
    private List<PreferBean> preferBeens = new ArrayList<>();
    private List<PreferModel> preferModels = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private PreferAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private FragmentManager fragmentManager;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_prefer;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView(savedInstanceState);
        initData();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvTopBarText = (TextView) mRootView.findViewById(R.id.tv_top_bar_text);
        mTvTopBarText.setText(R.string.prefer);

        Click click = new Click();
        mIvTopBarLeftBtn = (ImageView) mRootView.findViewById(R.id.top_bar_left_button);
        mIvTopBarLeftBtn.setImageResource(R.mipmap.back);
        mIvTopBarLeftBtn.setOnClickListener(click);

        mQueryPreferPresenter = new QueryPreferPresenter(queryPreferHandler);
        mQueryBookImgsPresenter = new QueryBookImgsPresenter(queryBookImgsHandler);

        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recyclerview);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new PreferAdapter(preferModels, R.layout.error);

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
        UserBean user = BmobUser.getCurrentUser(getActivity(), UserBean.class);
        mQueryPreferPresenter.queryPrefer(getActivity(), user, start, step);
    }

    @Override
    public void onItemClick(View view, int postion) {
        gotoBookDetail(postion);
    }

    private class Click implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.top_bar_left_button:
                    getActivity().onBackPressed();
                    break;
            }
        }
    }

    CallBackHandler queryPreferHandler = new CallBackHandler() {
        @Override
        public void handleSuccessMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCESS:
                    Bundle bundle = msg.getData();
                    List list = bundle.getParcelableArrayList(Constant.LIST);
                    if (list != null && list.size() > 0) {
                        preferBeens.clear();
                        preferBeens.addAll(list);
                        for (int i = 0, length = preferBeens.size(); i < length; i++) {
                            mQueryBookImgsPresenter.queryBookImgs(getActivity(), (PreferBean) list.get(i));
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
                    PreferBean preferBean = bundle.getParcelable(Constant.DATA);

                    PreferModel preferModel = new PreferModel();
                    preferModel.setPrefer(preferBean);
                    preferModel.setBookImgs(list);

                    if (isRefresh) {
                        preferModels.clear();
                        isRefresh = false;
                    }
                    preferModels.add(preferModel);

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
            if (lastVisibleItem >= totalItemCount - 4) {
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

    void gotoBookDetail(int position) {

        if (fragmentManager == null) {
            fragmentManager = getActivity().getSupportFragmentManager();
        }
        Fragment fragment = fragmentManager.findFragmentByTag(BookDetailFragment.TAG);
        //if (fragment == null) {
        fragment = BookDetailFragment.getInstance();

        BookModel bookModel = new BookModel();
        bookModel.setBook(preferModels.get(position).getPrefer().getBook());
        bookModel.setBookImgs(preferModels.get(position).getBookImgs());

        Bundle args = new Bundle();
        args.putParcelable(Constant.BOOK, bookModel);
        fragment.setArguments(args);
       /* } else {
            Bundle args = fragment.getArguments();

            BookModel bookModel = new BookModel();
            bookModel.setBook(preferModels.get(position).getPrefer().getBook());
            bookModel.setBookImgs(preferModels.get(position).getBookImgs());

            args.putParcelable(Constant.BOOK, bookModel);
        }*/
        Fragment form = fragmentManager.findFragmentByTag(PreferFragment.TAG);

        FragmentUtil.switchContentAddToBackStack(form, fragment, R.id.main_container, fragmentManager, BookDetailFragment.TAG);
    }
}
