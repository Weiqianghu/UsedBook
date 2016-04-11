package com.weiqianghu.usedbook.model.inf;

import android.content.Context;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by weiqianghu on 2016/2/24.
 */
public interface IUpdateBatchModel<T extends BmobObject> {
    boolean updateBatch(Context context, List<T> objects, UpdateListener updateListener);
}
