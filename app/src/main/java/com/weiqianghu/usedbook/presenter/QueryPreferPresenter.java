package com.weiqianghu.usedbook.presenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;

import com.weiqianghu.usedbook.model.entity.PreferBean;
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
public class QueryPreferPresenter extends CommonPresenter {

    private IQueryModel<PreferBean> mQueryModel;

    public QueryPreferPresenter(Handler handler) {
        super(handler);
        mQueryModel = new QueryModel<PreferBean>();
    }

    public void queryPrefer(Context context, PreferBean preferBean) {
        BmobQuery<PreferBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("user", preferBean.getUser());
        query1.addWhereEqualTo("book", preferBean.getBook());
        query1.addWhereEqualTo("isDelete", false);

        BmobQuery<PreferBean> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("user", preferBean.getUser());
        query2.addWhereEqualTo("shop", preferBean.getBook());
        query2.addWhereEqualTo("isDelete", false);

        List<BmobQuery<PreferBean>> queries = new ArrayList<>();
        queries.add(query1);
        queries.add(query2);

        BmobQuery<PreferBean> mainQuery = new BmobQuery<>();
        mainQuery.or(queries);

        FindListener<PreferBean> findListener = new FindListener<PreferBean>() {
            @Override
            public void onSuccess(List<PreferBean> list) {
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

        mQueryModel.query(context, mainQuery, findListener);
    }

    public void queryPrefer(Context context, UserBean userBean, int start, int step) {
        BmobQuery<PreferBean> mainQuery = new BmobQuery<>();
        mainQuery.addWhereEqualTo("user", userBean);
        mainQuery.addWhereEqualTo("isDelete", false);
        mainQuery.addWhereExists("book");
        mainQuery.include("book");
        mainQuery.setLimit(step);
        mainQuery.setSkip(start);

        /*BmobQuery<PreferBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("user", userBean);
        query1.addWhereEqualTo("isDelete", false);
        query1.addWhereExists("book");
        query1.include("book");

        BmobQuery<PreferBean> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("user", userBean);
        query2.addWhereEqualTo("isDelete", false);
        query2.addWhereExists("shop");
        query2.include("shop");

        List<BmobQuery<PreferBean>> queries = new ArrayList<>();
        queries.add(query1);
        queries.add(query2);

        BmobQuery<PreferBean> mainQuery = new BmobQuery<>();
        mainQuery.include("book");
        mainQuery.include("shop");
        mainQuery.setLimit(step);
        mainQuery.setSkip(start);
        mainQuery.or(queries);*/

        FindListener<PreferBean> findListener = new FindListener<PreferBean>() {
            @Override
            public void onSuccess(List<PreferBean> list) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(Constant.LIST, (ArrayList<? extends Parcelable>) list);
                handleSuccess(bundle);
            }

            @Override
            public void onError(int i, String s) {
                handleFailureMessage(i, s);
            }
        };

        mQueryModel.query(context, mainQuery, findListener);
    }
}
