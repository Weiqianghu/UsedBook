package com.weiqianghu.usedbook.model.inf;

import android.content.Context;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.DeleteListener;

/**
 * Created by weiqianghu on 2016/2/24.
 */
public interface IDeleteModel<T extends BmobObject> {
    boolean delete(T object, Context context, DeleteListener deleteListener);
}
