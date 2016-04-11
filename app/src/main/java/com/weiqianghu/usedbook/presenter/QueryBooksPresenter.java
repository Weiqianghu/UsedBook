package com.weiqianghu.usedbook.presenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;

import com.weiqianghu.usedbook.model.entity.BookBean;
import com.weiqianghu.usedbook.model.entity.ShopBean;
import com.weiqianghu.usedbook.model.entity.ShoppingCartBean;
import com.weiqianghu.usedbook.model.entity.UserBean;
import com.weiqianghu.usedbook.model.impl.QueryModel;
import com.weiqianghu.usedbook.model.inf.IQueryModel;
import com.weiqianghu.usedbook.util.Constant;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SQLQueryListener;

/**
 * Created by weiqianghu on 2016/3/12.
 */
public class QueryBooksPresenter extends CommonPresenter {
    private IQueryModel<BookBean> mQueryModel;

    public QueryBooksPresenter(Handler handler) {
        super(handler);
        mQueryModel = new QueryModel<BookBean>();
    }

    public void queryBooks(Context context, int start, int step) {
        FindListener<BookBean> findListener = new FindListener<BookBean>() {
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

        BmobQuery<BookBean> query = new BmobQuery<>();
        query.include("shop");
        query.addWhereEqualTo("isSell", true);
        query.setLimit(step);
        query.setSkip(start);
        query.order("-salesVolume,-createdAt");
        mQueryModel.query(context, query, findListener);
    }


    public void queryBooks(Context context, ShopBean shop, int start, int step) {
        FindListener<BookBean> findListener = new FindListener<BookBean>() {
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

        BmobQuery<BookBean> query = new BmobQuery<>();
        query.addWhereEqualTo("shop", shop);
        query.include("shop");
        query.setLimit(step);
        query.setSkip(start);
        query.order("-salesVolume,-createdAt");
        mQueryModel.query(context, query, findListener);
    }


    public void queryBooks(Context context, final ShoppingCartBean shoppingCartBean) {
        GetListener<BookBean> getListener = new GetListener<BookBean>() {

            @Override
            public void onFailure(int i, String s) {
                handleFailureMessage(i, s);
            }

            @Override
            public void onSuccess(BookBean bookBean) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constant.DATA, bookBean);
                bundle.putParcelable(Constant.SHOPPINGCART, shoppingCartBean);
                handleSuccess(bundle);
            }
        };

        BmobQuery<BookBean> query = new BmobQuery<>();
        String objectId = shoppingCartBean.getBook().getObjectIdStr();
        mQueryModel.query(context, query, getListener, objectId);
    }

    public void queryBooks(Context context, String searchPar, int start, int step) {
        FindListener<BookBean> getListener = new FindListener<BookBean>() {

            @Override
            public void onSuccess(List<BookBean> list) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(Constant.LIST, (ArrayList<? extends Parcelable>) list);
                handleSuccess(bundle);
            }

            @Override
            public void onError(int i, String s) {
                handleFailureMessage(i, s);
            }
        };

        BmobQuery<BookBean> query1 = new BmobQuery<>();
        query1.addWhereContains("bookName", searchPar);

        BmobQuery<BookBean> query2 = new BmobQuery<>();
        query2.addWhereContains("author", searchPar);

        BmobQuery<BookBean> query3 = new BmobQuery<>();
        query3.addWhereContains("press", searchPar);

        List<BmobQuery<BookBean>> queries = new ArrayList<BmobQuery<BookBean>>();
        queries.add(query1);
        queries.add(query2);
        queries.add(query3);

        BmobQuery<BookBean> mainQuery = new BmobQuery<BookBean>();
        mainQuery.or(queries);
        mainQuery.setLimit(step);
        mainQuery.setSkip(start);
        mainQuery.order("-salesVolume");

        mQueryModel.query(context, mainQuery, getListener);
    }

    public void sqlQueryBooks(Context context, String searchPar, int start, int step) {
        SQLQueryListener<BookBean> sqlQueryListener = new SQLQueryListener<BookBean>() {
            @Override
            public void done(BmobQueryResult bmobQueryResult, BmobException e) {
                if (e == null) {
                    List<BookBean> list = (List<BookBean>) bmobQueryResult.getResults();
                    if (list != null && list.size() > 0) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList(Constant.LIST, (ArrayList<? extends Parcelable>) list);
                        handleSuccess(bundle);
                    } else {
                        Log.i("smile", "查询成功，无数据返回");
                    }
                } else {
                    Log.i("smile", "错误码：" + e.getErrorCode() + "，错误描述：" + e.getMessage());
                    handleFailureMessage(e.getErrorCode(), e.getMessage());
                }
            }
        };
        String bql = "select * from BookBean where bookName like %?%  ";

        BmobQuery<BookBean> query = new BmobQuery<BookBean>();
        //设置SQL语句
        query.setSQL(bql);
        query.setPreparedParams(new Object[]{searchPar});
        mQueryModel.sqlQuery(context, query, sqlQueryListener
        );
    }
}
