package com.weiqianghu.usedbook.presenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.weiqianghu.usedbook.model.entity.FailureMessage;
import com.weiqianghu.usedbook.model.impl.UpdatePwdModel;
import com.weiqianghu.usedbook.model.inf.IUpdatePwdModel;
import com.weiqianghu.usedbook.util.Constant;

import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by weiqianghu on 2016/2/22.
 */
public class UpdatePwdPresenter {
    private IUpdatePwdModel mUpdatePwdModel;
    private Handler handler;

    public UpdatePwdPresenter(Handler handler){
        this.handler=handler;
        mUpdatePwdModel=new UpdatePwdModel();
    }

    public void updatePwd(Context context,String oldPwd,String newPwd){
        UpdateListener updateListener=new UpdateListener() {

            @Override
            public void onSuccess() {
                Message message = new Message();
                message.what = Constant.SUCCESS;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(int code, String msg) {
                Message message = new Message();
                message.what = Constant.FAILURE;

                FailureMessage failureMessage = new FailureMessage();
                failureMessage.setMsgCode(code);
                failureMessage.setMsg(msg);

                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.FAILURE_MESSAGE, failureMessage);

                message.setData(bundle);
                handler.sendMessage(message);
            }
        };
        mUpdatePwdModel.updatePwd(context,updateListener,oldPwd,newPwd);
    }
}
