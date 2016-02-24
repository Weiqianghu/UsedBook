package com.weiqianghu.usedbook.view.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.entity.ShoppingCartBean;
import com.weiqianghu.usedbook.presenter.IsLoginPresenter;
import com.weiqianghu.usedbook.presenter.ShoppingCartPresenter;
import com.weiqianghu.usedbook.presenter.adapter.CommonAdapter;
import com.weiqianghu.usedbook.view.ViewHolder;
import com.weiqianghu.usedbook.view.common.BaseFragment;
import com.weiqianghu.usedbook.view.view.IShoppingCartView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingCartFragment extends BaseFragment implements IShoppingCartView {


    private View mSuggestToLoginLayout;
    private IsLoginPresenter mIsLoginPresenter;

    private ListView mShoppingCartListView;

    private ShoppingCartPresenter mPresenter;
    private CommonAdapter<ShoppingCartBean> shoppingCartAdapter;

    private TextView mTotalMoney;

    private List<ShoppingCartBean> mData;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shopping_cart;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView(savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mSuggestToLoginLayout = mRootView.findViewById(R.id.ly_suggest_to_login);

        mIsLoginPresenter = new IsLoginPresenter();

        if (mPresenter == null) {
            mPresenter = new ShoppingCartPresenter(this);
        }
        mShoppingCartListView = (ListView) mRootView.findViewById(R.id.lv_shoppingcart);

        mPresenter.showShoppingCart();

        mTotalMoney = (TextView) mRootView.findViewById(R.id.tv_total_money);
        showTotalMoney();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mIsLoginPresenter.isLogin(getActivity())){//TODO 没有登陆
            mSuggestToLoginLayout.setVisibility(View.VISIBLE);
        }else {
            mSuggestToLoginLayout.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void showShoppingCart(final List<ShoppingCartBean> shoppingCartBeanList) {
        this.mData=shoppingCartBeanList;
        shoppingCartAdapter = new CommonAdapter<ShoppingCartBean>(getActivity(),
                shoppingCartBeanList, R.layout.item_shopping_cart) {
            @Override
            public void convert(ViewHolder helper, final ShoppingCartBean item) {
                helper.setText(R.id.tv_book_price, "￥" + item.getPrice());
                helper.setText(R.id.tv_number, String.valueOf(item.getNumber()));
                helper.setText(R.id.tv_subtotal, "￥" + item.getSubtotal());
                CheckBox cb = helper.getView(R.id.cb_check);
                cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            item.setChecked(true);

                            showTotalMoney();
                        } else {
                            item.setChecked(false);
                            showTotalMoney();
                        }
                    }
                });
                cb.setChecked(item.isChecked());

                ImageButton ib_add = helper.getView(R.id.ib_add);
                ib_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item.setNumber(item.getNumber() + 1);
                        item.setSubtotal(item.getSubtotal() + item.getPrice());
                        shoppingCartAdapter.notifyDataSetChanged();
                        if(item.isChecked()) {
                            showTotalMoney();
                        }
                    }
                });

                ImageButton ib_subtract = helper.getView(R.id.ib_subtract);
                ib_subtract.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.getNumber() > 1) {
                            item.setNumber(item.getNumber() - 1);
                            item.setSubtotal(item.getSubtotal() - item.getPrice());
                            shoppingCartAdapter.notifyDataSetChanged();
                            if(item.isChecked()) {
                                showTotalMoney();
                            }
                        }
                    }
                });

                Button btn_delete = helper.getView(R.id.btn_delete);
                btn_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mData.remove(item);
                        shoppingCartAdapter.notifyDataSetChanged();
                    }
                });
            }
        };
        mShoppingCartListView.setAdapter(shoppingCartAdapter);
    }

    private void showTotalMoney(){
        new AsyncTask<Void,Void,Double>() {

            @Override
            protected Double doInBackground(Void... params) {
                double currentCount = 0;
                for(int i=0,length=mData.size();i<length;i++){
                    if (mData.get(i).isChecked()){
                        currentCount+=mData.get(i).getSubtotal();
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
}
