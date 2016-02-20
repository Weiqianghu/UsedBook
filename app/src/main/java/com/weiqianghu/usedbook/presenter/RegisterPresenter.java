package com.weiqianghu.usedbook.presenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.weiqianghu.usedbook.model.entity.FailureMessage;
import com.weiqianghu.usedbook.model.impl.RegisterModel;
import com.weiqianghu.usedbook.model.inf.IRegisterModel;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.view.view.IRegisterView;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by weiqianghu on 2016/2/20.
 */
public class RegisterPresenter {
    private IRegisterModel mRegisterModel;
    private IRegisterView mRegisterView;
    private Handler handler;

    public RegisterPresenter(IRegisterView iRegisterView,Handler handler){
        this.mRegisterView=iRegisterView;
        mRegisterModel=new RegisterModel();
        this.handler=handler;
    }

    public void register(Context context,String mobileNo, String smsCode, String password){
        SaveListener saveListener=new SaveListener() {
            @Override
            public void onSuccess() {
                Message message=new Message();
                message.what= Constant.SUCCESS;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(int i, String s) {
                Message message=new Message();
                message.what= Constant.FAILURE;

                FailureMessage failureMessage=new FailureMessage();
                failureMessage.setMsgCode(i);
                failureMessage.setMsg(s);

                Bundle bundle=new Bundle();
                bundle.putSerializable(Constant.FAILURE_MESSAGE,failureMessage);

                message.setData(bundle);
                handler.sendMessage(message);
            }
        };

        mRegisterModel.register(context,saveListener,mobileNo,smsCode,password);
    }
}
