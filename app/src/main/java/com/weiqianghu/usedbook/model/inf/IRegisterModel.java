package com.weiqianghu.usedbook.model.inf;

import android.content.Context;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by weiqianghu on 2016/2/19.
 */
public interface IRegisterModel {
    boolean register(Context context, SaveListener saveListener,String mobileNo, String smsCode, String password);
}
