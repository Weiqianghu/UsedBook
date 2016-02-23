package com.weiqianghu.usedbook.model.inf;

import android.content.Context;

import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by weiqianghu on 2016/2/22.
 */
public interface IUpdatePwdModel {
    boolean updatePwd(Context context, UpdateListener updateListener, String oldPwd, String newPwd);
}
