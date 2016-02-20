package com.weiqianghu.usedbook.presenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.weiqianghu.usedbook.model.entity.FailureMessage;
import com.weiqianghu.usedbook.model.impl.LoginModel;
import com.weiqianghu.usedbook.model.inf.ILoginModel;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.view.view.ILoginView;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by 胡伟强 on 2016/1/27.
 */
public class LoginPresenter {
    private ILoginView mLoginView;
    private ILoginModel mLoginModel;
    private Handler handler;

    public LoginPresenter(ILoginView iLoginView,Handler handler){
        this.mLoginView=iLoginView;
        mLoginModel=new LoginModel();
        this.handler=handler;
    }

    public void Login(Context context,String username, String password){
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

       boolean isLoginSuccessed= mLoginModel.login(context,saveListener,username,password);
        mLoginView.login(isLoginSuccessed);
    }
}
