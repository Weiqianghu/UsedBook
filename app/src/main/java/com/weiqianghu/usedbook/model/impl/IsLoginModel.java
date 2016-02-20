package com.weiqianghu.usedbook.model.impl;

import android.content.Context;

import com.weiqianghu.usedbook.model.entity.UserBean;
import com.weiqianghu.usedbook.model.inf.IIsLoginModel;

import java.util.Random;

import cn.bmob.v3.BmobUser;

/**
 * Created by 胡伟强 on 2016/1/28.
 */
public class IsLoginModel implements IIsLoginModel {

    @Override
    public boolean isLogin(Context context) {
        UserBean userBean=  BmobUser.getCurrentUser(context,UserBean.class);
        return userBean==null;
    }
}
