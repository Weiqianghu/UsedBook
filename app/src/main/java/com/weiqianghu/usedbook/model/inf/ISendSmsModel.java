package com.weiqianghu.usedbook.model.inf;

import android.content.Context;

import cn.bmob.v3.listener.RequestSMSCodeListener;

/**
 * Created by weiqianghu on 2016/2/22.
 */
public interface ISendSmsModel {
    boolean sendSmsCode(Context context, RequestSMSCodeListener requestSMSCodeListener,String mobileNo,String pattern);
}
