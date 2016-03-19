package com.weiqianghu.usedbook.presenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;

import com.weiqianghu.usedbook.model.entity.ShoppingCartBean;
import com.weiqianghu.usedbook.model.entity.UserBean;
import com.weiqianghu.usedbook.model.impl.QueryModel;
import com.weiqianghu.usedbook.model.inf.IQueryModel;
import com.weiqianghu.usedbook.util.Constant;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by weiqianghu on 2016/3/19.
 */
public class QueryShoppingCartPresenter extends CommonPresenter {

    private IQueryModel<ShoppingCartBean> mQueryModel;

    public QueryShoppingCartPresenter(Handler handler) {
        super(handler);
        mQueryModel = new QueryModel<ShoppingCartBean>();
    }

    public void queryShoppingCart(Context context, ShoppingCartBean shoppingCartBean) {
        BmobQuery<ShoppingCartBean> query = new BmobQuery<>();
        query.addWhereEqualTo("user", shoppingCartBean.getUser());
        query.addWhereEqualTo("book", shoppingCartBean.getBook());
        query.addWhereEqualTo("isOrder", false);

        FindListener<ShoppingCartBean> findListener = new FindListener<ShoppingCartBean>() {
            @Override
            public void onSuccess(List<ShoppingCartBean> list) {
                Bundle bundle = new Bundle();
                if (list != null && list.size() > 0) {
                    bundle.putInt(Constant.EXIST, Constant.TRUE);
                } else {
                    bundle.putInt(Constant.EXIST, Constant.FALSE);
                }

                handleSuccess(bundle);
            }

            @Override
            public void onError(int i, String s) {
                handleFailureMessage(i, s);
            }
        };

        mQueryModel.query(context, query, findListener);
    }

    public void queryShoppingCart(Context context, UserBean userBean, int start, int step) {
        BmobQuery<ShoppingCartBean> query = new BmobQuery<>();
        query.addWhereEqualTo("user", userBean);
        query.addWhereEqualTo("isOrder", false);
        query.include("book");
        query.setLimit(step);
        query.setSkip(start);

        FindListener<ShoppingCartBean> findListener = new FindListener<ShoppingCartBean>() {
            @Override
            public void onSuccess(List<ShoppingCartBean> list) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(Constant.LIST, (ArrayList<? extends Parcelable>) list);
                handleSuccess(bundle);
            }

            @Override
            public void onError(int i, String s) {
                handleFailureMessage(i, s);
            }
        };

        mQueryModel.query(context, query, findListener);
    }
}
