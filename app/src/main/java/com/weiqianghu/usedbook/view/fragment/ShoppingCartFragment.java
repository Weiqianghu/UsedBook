package com.weiqianghu.usedbook.view.fragment;


import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.entity.BookImgsBean;
import com.weiqianghu.usedbook.model.entity.ShoppingCartBean;
import com.weiqianghu.usedbook.model.entity.ShoppingCartModel;
import com.weiqianghu.usedbook.model.entity.UserBean;
import com.weiqianghu.usedbook.presenter.IsLoginPresenter;
import com.weiqianghu.usedbook.presenter.QueryBookImgsPresenter;
import com.weiqianghu.usedbook.presenter.QueryShoppingCartPresenter;
import com.weiqianghu.usedbook.presenter.UpdatePresenter;
import com.weiqianghu.usedbook.presenter.adapter.CommonAdapter;
import com.weiqianghu.usedbook.util.CallBackHandler;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.view.ViewHolder;
import com.weiqianghu.usedbook.view.common.BaseFragment;
import com.weiqianghu.usedbook.view.view.IUpdateView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingCartFragment extends BaseFragment implements IUpdateView {


    private View mSuggestToLoginLayout;
    private IsLoginPresenter mIsLoginPresenter;

    private ListView mShoppingCartListView;
    private TextView mTotalMoney;

    private List<ShoppingCartBean> mShoppingCartBeans = new ArrayList<>();
    private List<ShoppingCartModel> mShoppingCartModels = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int count = 0;
    private static final int STEP = 30;
    private boolean isRefresh = false;
    private QueryShoppingCartPresenter mQueryShoppingCartPresenter;
    private QueryBookImgsPresenter mQueryBookImgsPresenter;
    private UpdatePresenter<ShoppingCartBean> mUpdatePresenter;

    private CommonAdapter<ShoppingCartModel> mShoppingCartAdapter;

    private UserBean lastUser = null;

    @Override
    protected int getLayoutId() {
        Fresco.initialize(getActivity());
        return R.layout.fragment_shopping_cart;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initAdapter();
        initView(savedInstanceState);
        initData();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mSuggestToLoginLayout = mRootView.findViewById(R.id.ly_suggest_to_login);

        mIsLoginPresenter = new IsLoginPresenter();

        mShoppingCartListView = (ListView) mRootView.findViewById(R.id.lv_shoppingcart);
        mShoppingCartListView.setEmptyView(mRootView.findViewById(R.id.empty_view));
        mShoppingCartListView.setAdapter(mShoppingCartAdapter);
        mShoppingCartListView.setOnScrollListener(new AbsListView.OnScrollListener() {
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

        mTotalMoney = (TextView) mRootView.findViewById(R.id.tv_total_money);
        showTotalMoney();

        mQueryShoppingCartPresenter = new QueryShoppingCartPresenter(queryShoppingCartHandler);
        mQueryBookImgsPresenter = new QueryBookImgsPresenter(queryBookImgsHandler);
        mUpdatePresenter = new UpdatePresenter<>(this, delectShoppingCartHandler);

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

        mShoppingCartListView.setAdapter(mShoppingCartAdapter);
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
        lastUser = user;
        mQueryShoppingCartPresenter.queryShoppingCart(getActivity(), user, start, step);
    }

    @Override
    public void onResume() {
        super.onResume();
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                UserBean currentUser = BmobUser.getCurrentUser(getActivity(), UserBean.class);
                if (lastUser != null && currentUser != null && lastUser != currentUser) {
                    initData();
                }
                return mIsLoginPresenter.isLogin(getActivity());
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);

                if (aBoolean) {
                    mSuggestToLoginLayout.setVisibility(View.VISIBLE);
                } else {
                    mSuggestToLoginLayout.setVisibility(View.INVISIBLE);
                }
            }
        }.execute();

    }

    CallBackHandler queryShoppingCartHandler = new CallBackHandler() {
        @Override
        public void handleSuccessMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCESS:
                    Bundle bundle = msg.getData();
                    List list = bundle.getParcelableArrayList(Constant.LIST);
                    if (list != null && list.size() > 0) {
                        mShoppingCartBeans.clear();
                        mShoppingCartBeans.addAll(list);
                        for (int i = 0, length = mShoppingCartBeans.size(); i < length; i++) {
                            mQueryBookImgsPresenter.queryBookImgs(getActivity(), (ShoppingCartBean) list.get(i));
                        }
                    } else {
                        mSwipeRefreshLayout.setRefreshing(false);
                        if (isRefresh) {
                            mShoppingCartModels.clear();
                            mShoppingCartAdapter.notifyDataSetChanged();
                        }
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
                    ShoppingCartBean shoppingCartBean = bundle.getParcelable(Constant.DATA);

                    ShoppingCartModel shoppingCartModel = new ShoppingCartModel();
                    shoppingCartModel.setShoppingCartBean(shoppingCartBean);
                    shoppingCartModel.setBookImgs(list);

                    if (isRefresh) {
                        mShoppingCartModels.clear();
                        isRefresh = false;
                    }
                    mShoppingCartModels.add(shoppingCartModel);

                    mShoppingCartAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
            }
        }

        @Override
        public void handleFailureMessage(String msg) {
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };

    CallBackHandler delectShoppingCartHandler = new CallBackHandler() {
        @Override
        public void handleSuccessMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCESS:
                    mShoppingCartAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void handleFailureMessage(String msg) {
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };

    private void initAdapter() {
        mShoppingCartAdapter = new CommonAdapter<ShoppingCartModel>(getActivity(), mShoppingCartModels, R.layout.item_shopping_cart) {
            @Override
            public void convert(ViewHolder helper, final ShoppingCartModel item) {

                final ShoppingCartBean shoppingCartBean = item.getShoppingCartBean();

                helper.setText(R.id.tv_book_name, shoppingCartBean.getBook().getBookName());
                helper.setText(R.id.tv_book_price, "￥" + shoppingCartBean.getPrice());
                helper.setText(R.id.tv_number, String.valueOf(shoppingCartBean.getNumber()));
                helper.setText(R.id.tv_subtotal, "￥" + shoppingCartBean.getSubtotal());

                List<BookImgsBean> imgs = item.getBookImgs();
                if (imgs != null && imgs.size() > 0) {
                    Uri uri = Uri.parse(imgs.get(0).getImg());
                    helper.setImageForSimpleDraweeViewUri(R.id.iv_book, uri);
                } else {
                    Uri uri = Uri.parse("res://com.weiqianghu.usedbook_shop/" + R.mipmap.upload_img);
                    helper.setImageForSimpleDraweeViewUri(R.id.iv_book, uri);
                }

                CheckBox cb = helper.getView(R.id.cb_check);
                cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            shoppingCartBean.setChecked(true);

                            showTotalMoney();
                        } else {
                            shoppingCartBean.setChecked(false);
                            showTotalMoney();
                        }
                    }
                });
                cb.setChecked(shoppingCartBean.isChecked());

                ImageButton ib_add = helper.getView(R.id.ib_add);
                ib_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shoppingCartBean.setNumber(shoppingCartBean.getNumber() + 1);
                        shoppingCartBean.setSubtotal(shoppingCartBean.getSubtotal() + shoppingCartBean.getPrice());
                        mShoppingCartAdapter.notifyDataSetChanged();
                        if (shoppingCartBean.isChecked()) {
                            showTotalMoney();
                        }
                    }
                });

                ImageButton ib_subtract = helper.getView(R.id.ib_subtract);
                ib_subtract.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (shoppingCartBean.getNumber() > 1) {
                            shoppingCartBean.setNumber(shoppingCartBean.getNumber() - 1);
                            shoppingCartBean.setSubtotal(shoppingCartBean.getSubtotal() - shoppingCartBean.getPrice());
                            mShoppingCartAdapter.notifyDataSetChanged();
                            if (shoppingCartBean.isChecked()) {
                                showTotalMoney();
                            }
                        }
                    }
                });

                Button btn_delete = helper.getView(R.id.btn_delete);
                btn_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("确定要从购物车中删除这件商品");
                        builder.setIcon(android.R.drawable.ic_dialog_info);
                        builder.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mShoppingCartModels.remove(item);
                                deleteShoppingCartModel(item);
                            }
                        });
                        builder.setNegativeButton(R.string.cancel, null);
                        builder.show();
                    }
                });
            }
        };
    }

    private void deleteShoppingCartModel(ShoppingCartModel item) {
        ShoppingCartBean shoppingCartBean = item.getShoppingCartBean();
        shoppingCartBean.setOrder(true);
        mUpdatePresenter.update(getActivity(), shoppingCartBean, shoppingCartBean.getObjectId());
    }

    private void showTotalMoney() {
        new AsyncTask<Void, Void, Double>() {

            @Override
            protected Double doInBackground(Void... params) {
                double currentCount = 0;
                for (int i = 0, length = mShoppingCartModels.size(); i < length; i++) {
                    ShoppingCartBean shoppingCartBean = mShoppingCartModels.get(i).getShoppingCartBean();
                    if (shoppingCartBean.isChecked()) {
                        currentCount += shoppingCartBean.getSubtotal();
                    }
                }
                return currentCount;
            }

            @Override
            protected void onPostExecute(Double aDouble) {
                super.onPostExecute(aDouble);
                mTotalMoney.setText(String.valueOf(aDouble));
            }
        }.execute();
    }

    private void loadMore() {
        count++;
        queryData(count * STEP, STEP);
    }
}
