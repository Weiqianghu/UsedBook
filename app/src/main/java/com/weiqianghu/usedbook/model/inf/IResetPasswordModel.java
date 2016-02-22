package com.weiqianghu.usedbook.model.inf;

import android.content.Context;

import cn.bmob.v3.listener.ResetPasswordByCodeListener;

/**
 * Created by weiqianghu on 2016/2/22.
 */
public interface IResetPasswordModel {
    boolean resetPwd(Context context, ResetPasswordByCodeListener resetPasswordByCodeListener,String smsCode,String password);
}
