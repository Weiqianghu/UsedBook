package com.weiqianghu.usedbook.model.inf;

import android.content.Context;

import cn.bmob.v3.listener.VerifySMSCodeListener;

/**
 * Created by weiqianghu on 2016/2/22.
 */
public interface IVerifySmsModel {
    boolean verifySms(Context context, VerifySMSCodeListener verifySMSCodeListener,String smsCode, String mobileNo);
}
