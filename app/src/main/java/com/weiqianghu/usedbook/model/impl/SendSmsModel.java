package com.weiqianghu.usedbook.model.impl;

import android.content.Context;

import com.weiqianghu.usedbook.model.inf.ISendSmsModel;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.listener.RequestSMSCodeListener;

/**
 * Created by weiqianghu on 2016/2/22.
 */
public class SendSmsModel implements ISendSmsModel {
    @Override
    public boolean sendSmsCode(Context context, RequestSMSCodeListener requestSMSCodeListener, String mobileNo,String pattern) {
        BmobSMS.requestSMSCode(context, mobileNo, pattern,requestSMSCodeListener);
        return false;
    }
}
