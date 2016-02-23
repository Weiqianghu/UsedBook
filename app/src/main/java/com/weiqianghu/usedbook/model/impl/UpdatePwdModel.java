package com.weiqianghu.usedbook.model.impl;

import android.content.Context;

import com.weiqianghu.usedbook.model.inf.IUpdatePwdModel;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by weiqianghu on 2016/2/22.
 */
public class UpdatePwdModel implements IUpdatePwdModel {
    @Override
    public boolean updatePwd(Context context, UpdateListener updateListener, String oldPwd, String newPwd) {
        BmobUser.updateCurrentUserPassword(context, oldPwd, newPwd, updateListener);
        return true;
    }
}
