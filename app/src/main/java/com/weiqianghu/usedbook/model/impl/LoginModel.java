package com.weiqianghu.usedbook.model.impl;

import android.util.Log;

import com.weiqianghu.usedbook.model.inf.ILoginModel;

/**
 * Created by 胡伟强 on 2016/1/27.
 */
public class LoginModel implements ILoginModel {
    @Override
    public boolean login(String username, String password) {
        //TODO 运用RxJava进行回调操作
        Log.d("login","username:"+username+",password:"+password);
        return true;
    }
}
