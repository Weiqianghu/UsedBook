package com.weiqianghu.usedbook.presenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;

import com.weiqianghu.usedbook.model.chat_model.bean.User;
import com.weiqianghu.usedbook.model.entity.ShopBean;
import com.weiqianghu.usedbook.model.entity.UserBean;
import com.weiqianghu.usedbook.model.impl.QueryModel;
import com.weiqianghu.usedbook.model.inf.IQueryModel;
import com.weiqianghu.usedbook.util.Constant;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;

/**
 * Created by weiqianghu on 2016/4/2.
 */
public class QueryUserPresenter extends CommonPresenter {
    private IQueryModel<UserBean> mQueryModel;

    public QueryUserPresenter(Handler handler) {
        super(handler);
        mQueryModel = new QueryModel<UserBean>();
    }

    public void queryUser(Context context, ShopBean shopBean) {
        BmobQuery<UserBean> query = new BmobQuery<>();
        query.addWhereEqualTo("shop", shopBean);
        query.include("shop");
        FindListener<UserBean> findListener = new FindListener<UserBean>() {
            @Override
            public void onSuccess(List<UserBean> list) {
                if (list != null && list.size() > 0) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(Constant.LIST, (ArrayList<? extends Parcelable>) list);
                    handleSuccess(bundle);
                } else {
                    handleSuccess();
                }
            }

            @Override
            public void onError(int i, String s) {
                handleFailureMessage(i, s);
            }
        };

        mQueryModel.query(context, query, findListener);
    }

    public void QueryUser(Context context, String objectId) {
        GetListener<UserBean> getListener = new GetListener<UserBean>() {

            @Override
            public void onFailure(int i, String s) {
                handleFailureMessage(i, s);
            }

            @Override
            public void onSuccess(UserBean userBean) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constant.DATA, userBean);
                handleSuccess(bundle);
            }
        };

        BmobQuery<UserBean> query = new BmobQuery<>();
        mQueryModel.query(context, query, getListener, objectId);
    }
}
