package com.weiqianghu.usedbook.util;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.weiqianghu.usedbook.model.entity.FailureMessage;

/**
 * Created by weiqianghu on 2016/2/22.
 */
public  class CallBackHandler extends Handler {
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case Constant.FAILURE:
                Bundle bundle = msg.getData();
                FailureMessage failureMessage = (FailureMessage) bundle.getSerializable(Constant.FAILURE_MESSAGE);
                String failureMsg = failureMessage.getMsg();
                handleFailureMessage(failureMsg);
                break;
        }
        handleSuccessMessage(msg);
    }

    public  void handleSuccessMessage(Message msg){}

    public void handleFailureMessage(String msg){}
}
