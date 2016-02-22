package com.weiqianghu.usedbook.model.impl;

import android.content.Context;

import com.weiqianghu.usedbook.model.inf.IVerifySmsModel;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.listener.VerifySMSCodeListener;

/**
 * Created by weiqianghu on 2016/2/22.
 */
public class VerifySmsModel implements IVerifySmsModel {
    @Override
    public boolean verifySms(Context context, VerifySMSCodeListener verifySMSCodeListener,String smsCode, String mobileNo) {
        BmobSMS.verifySmsCode(context,mobileNo, smsCode, verifySMSCodeListener);
        return true;
    }
}
