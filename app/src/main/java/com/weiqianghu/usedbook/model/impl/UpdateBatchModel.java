package com.weiqianghu.usedbook.model.impl;

import android.content.Context;

import com.weiqianghu.usedbook.model.inf.IUpdateBatchModel;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by weiqianghu on 2016/2/24.
 */
public class UpdateBatchModel implements IUpdateBatchModel {
    @Override
    public boolean updateBatch(Context context, List objects, UpdateListener updateListener) {
        new BmobObject().updateBatch(context,objects,updateListener);
        return true;
    }
}
