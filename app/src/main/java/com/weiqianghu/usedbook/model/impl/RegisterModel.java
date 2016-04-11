package com.weiqianghu.usedbook.model.impl;

import android.content.Context;

import com.weiqianghu.usedbook.model.entity.UserBean;
import com.weiqianghu.usedbook.model.inf.IRegisterModel;
import com.weiqianghu.usedbook.util.Constant;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by weiqianghu on 2016/2/19.
 */
public class RegisterModel implements IRegisterModel {
    @Override
    public boolean register(Context context, SaveListener saveListener, String mobileNo, String smsCode, String password) {
        UserBean user = new UserBean();
        user.setUsername(mobileNo);
        user.setPassword(password);
        user.setMobilePhoneNumber(mobileNo);
        user.setAddress("");
        user.setAge(0);
        user.setImg("");
        user.setSex(true);
        user.setLatitude(0.0);
        user.setLongitude(0.0);
        user.setShop(false);
        user.setRole(Constant.ROLE_USER);
        user.signUp(context, saveListener);

        user.signUp(context, saveListener);
        return true;
    }
}
