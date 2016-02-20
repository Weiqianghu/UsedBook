package com.weiqianghu.usedbook.model.impl;

import android.content.Context;

import com.weiqianghu.usedbook.model.entity.UserBean;
import com.weiqianghu.usedbook.model.inf.IRegisterModel;

import cn.bmob.v3.listener.SaveListener;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;

/**
 * Created by weiqianghu on 2016/2/19.
 */
public class RegisterModel implements IRegisterModel {
    @Override
    public boolean register(Context context,SaveListener saveListener,String mobileNo, String smsCode, String password) {
        UserBean user=new UserBean();
        user.setUsername(mobileNo);
        user.setPassword(password);
        user.setAddress("");
        user.setAge(0);
        user.setImg("");
        user.setSex(true);
        user.setLatitude(0.0);
        user.setLongitude(0.0);
        user.signUp(context,saveListener);
        return true;
    }
}
