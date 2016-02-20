package com.weiqianghu.usedbook.model.impl;

import android.content.Context;
import android.util.Log;

import com.weiqianghu.usedbook.model.entity.UserBean;
import com.weiqianghu.usedbook.model.inf.ILoginModel;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by 胡伟强 on 2016/1/27.
 */
public class LoginModel implements ILoginModel {
    @Override
    public boolean login(Context context, SaveListener saveListener,String username, String password) {
        UserBean userBean=new UserBean();
        userBean.setUsername(username);
        userBean.setPassword(password);
        userBean.login(context,saveListener);
        return true;
    }
}
