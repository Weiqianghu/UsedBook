package com.weiqianghu.usedbook.model.impl;

import android.content.Context;

import com.weiqianghu.usedbook.model.inf.IDeleteModel;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.DeleteListener;

/**
 * Created by weiqianghu on 2016/2/24.
 */
public class DeelteModel implements IDeleteModel {
    @Override
    public boolean delete(BmobObject object, Context context, DeleteListener deleteListener) {
        object.delete(context,deleteListener);
        return true;
    }
}
