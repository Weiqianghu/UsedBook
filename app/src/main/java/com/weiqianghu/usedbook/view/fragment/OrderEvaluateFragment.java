package com.weiqianghu.usedbook.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.entity.OrderBean;
import com.weiqianghu.usedbook.model.entity.OrderModel;
import com.weiqianghu.usedbook.presenter.QueryBookImgsPresenter;
import com.weiqianghu.usedbook.presenter.QueryOrderPresenter;
import com.weiqianghu.usedbook.presenter.adapter.OrderAdapter;
import com.weiqianghu.usedbook.util.CallBackHandler;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.view.activity.CommentActivity;
import com.weiqianghu.usedbook.view.common.BaseFragment;
import com.weiqianghu.usedbook.view.customview.EmptyRecyclerView;
import com.weiqianghu.usedbook.view.view.IRecycleViewItemClickListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderEvaluateFragment extends BaseFragment implements IRecycleViewItemClickListener {
    public static final String TAG = OrderEvaluateFragment.class.getSimpleName();

    private static final String ORDER_STATE = "evaluate";

    private TextView mEmptyTv;

    private List<OrderModel> mData = new ArrayList();
    private List<OrderBean> mOrders = new ArrayList<>();
    private EmptyRecyclerView mRecyclerView;
    private OrderAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private QueryOrderPresenter mQueryOrderPresenter;
    private QueryBookImgsPresenter mQueryBookImgsPresenter;

    private boolean isRefresh = false;
    private int count = 0;
    private static final int STEP = 15;

    private FragmentManager mFragmentManager;


    @Override
    protected int getLayoutId() {
        Fresco.initialize(getActivity());
        return R.layout.fragment_order_evaluate;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        if (isFirstIn) {
            initView(savedInstanceState);
            isFirstIn = false;
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        mEmptyTv = (TextView) mRootView.findViewById(R.id.tv_empty);
        mEmptyTv.setText(R.string.this_order_empty);

        mRecyclerView = (EmptyRecyclerView) mRootView.findViewById(R.id.recyclerview);
        View empty = mRootView.findViewById(R.id.book_empty);
        mRecyclerView.setEmptyView(empty);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new OrderAdapter(mData, R.layout.item_order);

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

        mQueryOrderPresenter = new QueryOrderPresenter(queryOrdersHandler);
        mQueryBookImgsPresenter = new QueryBookImgsPresenter(queryBookImgsHandler);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        }, 500);

    }

    private void initData() {
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
        mQueryOrderPresenter.queryOrders(getActivity(), start, step, ORDER_STATE);
    }

    private void loadMore() {
        count++;
        queryData(count * STEP, STEP);
    }

    @Override
    public void onItemClick(View view, int postion) {
        gotoComment(postion);
    }

    private void gotoComment(int postion) {
        OrderModel orderModel = mData.get(postion);

        orderModel.getOrderBean().getShop().setObjectIdStr(orderModel.getOrderBean().getShop().getObjectId());
        orderModel.getOrderBean().getAddress().setObjectIdStr(orderModel.getOrderBean().getAddress().getObjectId());
        orderModel.getOrderBean().getBook().setObjectIdStr(orderModel.getOrderBean().getBook().getObjectId());
        orderModel.getOrderBean().setObjectIdStr(orderModel.getOrderBean().getObjectId());


        Intent intent = new Intent(getActivity(), CommentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.DATA, orderModel);
        intent.putExtra(Constant.DATA, bundle);
        startActivity(intent);
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


    CallBackHandler queryOrdersHandler = new CallBackHandler() {
        @Override
        public void handleSuccessMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCESS:
                    Bundle bundle = msg.getData();
                    List list = bundle.getParcelableArrayList(Constant.LIST);
                    if (list != null && list.size() > 0) {
                        mOrders.clear();
                        mOrders.addAll(list);
                        for (int i = 0, length = mOrders.size(); i < length; i++) {
                            mQueryBookImgsPresenter.queryBookImgs(getActivity(), (OrderBean) list.get(i));
                        }
                    } else {
                        if (isRefresh) {
                            mData.clear();
                            mAdapter.notifyDataSetChanged();
                        }
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
                    OrderBean orderBean = bundle.getParcelable(Constant.BOOK);

                    OrderModel orderModel = new OrderModel();
                    orderModel.setOrderBean(orderBean);
                    orderModel.setBookImgs(list);

                    if (isRefresh) {
                        mData.clear();
                        isRefresh = false;
                    }
                    mData.add(orderModel);
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
}

