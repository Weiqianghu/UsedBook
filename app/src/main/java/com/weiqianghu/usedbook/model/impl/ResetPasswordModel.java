package com.weiqianghu.usedbook.model.impl;

import android.content.Context;

import com.weiqianghu.usedbook.model.entity.UserBean;
import com.weiqianghu.usedbook.model.inf.IResetPasswordModel;

import cn.bmob.v3.listener.ResetPasswordByCodeListener;

/**
 * Created by weiqianghu on 2016/2/22.
 */
public class ResetPasswordModel implements IResetPasswordModel {
    @Override
    public boolean resetPwd(Context context, ResetPasswordByCodeListener resetPasswordByCodeListener, String smsCode, String password) {
        UserBean.resetPasswordBySMSCode(context,smsCode,password,resetPasswordByCodeListener);
        return true;
    }
}
