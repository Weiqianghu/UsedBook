package com.weiqianghu.usedbook.presenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.weiqianghu.usedbook.model.entity.FailureMessageModel;
import com.weiqianghu.usedbook.model.impl.SendSmsModel;
import com.weiqianghu.usedbook.model.inf.ISendSmsModel;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.view.view.ISendSmsCodeView;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.RequestSMSCodeListener;

/**
 * Created by weiqianghu on 2016/2/22.
 */
public class SendSmsCodePresenter {
    private ISendSmsModel mSendSmsCodeModel;
    private ISendSmsCodeView mSendSmsView;
    private Handler handler;

    public SendSmsCodePresenter(ISendSmsCodeView sendSmsCodeView, Handler handler){
        this.mSendSmsView=sendSmsCodeView;
        this.handler=handler;
        mSendSmsCodeModel=new SendSmsModel();
    }

    public void sendSmsCode(Context context,String mobileNo,String pattern){
        RequestSMSCodeListener requestSMSCodeListener=new RequestSMSCodeListener(){

            @Override
            public void done(Integer i, BmobException e) {
                if(e==null){//验证码发送成功
                    Message message=new Message();
                    message.what= Constant.SUCCESS;
                    handler.sendMessage(message);
                }else {
                    Message message=new Message();
                    message.what= Constant.FAILURE;

                    FailureMessageModel failureMessageModel =new FailureMessageModel();
                    failureMessageModel.setMsgCode(e.getErrorCode());
                    failureMessageModel.setMsg(e.getLocalizedMessage());

                    Bundle bundle=new Bundle();
                    bundle.putSerializable(Constant.FAILURE_MESSAGE, failureMessageModel);

                    message.setData(bundle);
                    handler.sendMessage(message);
                }
            }
        };

        mSendSmsCodeModel.sendSmsCode(context,requestSMSCodeListener,mobileNo,pattern);
    }

}
