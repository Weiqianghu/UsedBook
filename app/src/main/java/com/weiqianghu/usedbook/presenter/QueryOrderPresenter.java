package com.weiqianghu.usedbook.presenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;


import com.weiqianghu.usedbook.model.entity.OrderBean;
import com.weiqianghu.usedbook.model.entity.ShopBean;
import com.weiqianghu.usedbook.model.entity.UserBean;
import com.weiqianghu.usedbook.model.impl.QueryModel;
import com.weiqianghu.usedbook.model.inf.IQueryModel;
import com.weiqianghu.usedbook.util.Constant;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by weiqianghu on 2016/3/25.
 */
public class QueryOrderPresenter extends CommonPresenter {
    private IQueryModel<OrderBean> mQueryModel;

    public QueryOrderPresenter(Handler handler) {
        super(handler);
        mQueryModel = new QueryModel<OrderBean>();
    }


    public void queryOrders(final Context context, int start, int step, String orderState) {
        FindListener<OrderBean> findListener = new FindListener<OrderBean>() {
            @Override
            public void onSuccess(List list) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(Constant.LIST, (ArrayList<? extends Parcelable>) list);
                handleSuccess(bundle);
            }

            @Override
            public void onError(int i, String s) {
                handleFailureMessage(i, s);
            }
        };

        UserBean currentUser = BmobUser.getCurrentUser(context, UserBean.class);

        BmobQuery<OrderBean> query = new BmobQuery<>();
        query.addWhereEqualTo("user", currentUser);
        query.addWhereEqualTo("orderState", orderState);
        query.include("book");
        query.setLimit(step);
        query.setSkip(start);
        query.order("-updatedAt");
        mQueryModel.query(context, query, findListener);
    }
}
