package com.weiqianghu.usedbook.model.inf;

import android.content.Context;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SQLQueryListener;

/**
 * Created by weiqianghu on 2016/2/23.
 */
public interface IQueryModel<T extends BmobObject> {
    boolean query(Context context, BmobQuery<T> query, FindListener<T> findListener);

    boolean query(Context context, BmobQuery<T> query, GetListener<T> getListener,String objectId);

    void sqlQuery(Context context, BmobQuery<T> query, SQLQueryListener<T> sqlQueryListener);
}
