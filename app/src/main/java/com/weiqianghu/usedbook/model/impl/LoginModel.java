package com.weiqianghu.usedbook.model.impl;

import android.content.Context;

import com.weiqianghu.usedbook.model.entity.UserBean;
import com.weiqianghu.usedbook.model.inf.ILoginModel;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by 胡伟强 on 2016/1/27.
 */
public class LoginModel implements ILoginModel {
    @Override
    public boolean login(Context context, LogInListener logInListener, String mobileNo, String password) {
        BmobUser.loginByAccount(context,mobileNo,password,logInListener);
        return true;
    }

    @Override
    public boolean updateLogin(Context context, SaveListener saveListener, UserBean userBean) {
       userBean.login(context,saveListener);
        return true;
    }
}
