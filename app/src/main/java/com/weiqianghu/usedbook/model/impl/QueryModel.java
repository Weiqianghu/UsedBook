package com.weiqianghu.usedbook.model.impl;

import android.content.Context;

import com.weiqianghu.usedbook.model.inf.IQueryModel;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SQLQueryListener;

/**
 * Created by weiqianghu on 2016/2/23.
 */
public class QueryModel<T extends BmobObject> implements IQueryModel {
    @Override
    public boolean query(Context context, BmobQuery query, FindListener findListener) {
        query.findObjects(context, findListener);
        return false;
    }

    @Override
    public boolean query(Context context, BmobQuery query, GetListener getListener, String objectId) {
        query.getObject(context, objectId, getListener);
        return true;
    }

    @Override
    public void sqlQuery(Context context, BmobQuery query, SQLQueryListener sqlQueryListener) {
        query.doSQLQuery(context, sqlQueryListener);
    }
}
