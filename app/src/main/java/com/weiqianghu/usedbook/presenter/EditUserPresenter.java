package com.weiqianghu.usedbook.presenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.weiqianghu.usedbook.model.entity.FailureMessage;
import com.weiqianghu.usedbook.model.entity.UserBean;
import com.weiqianghu.usedbook.model.impl.EditUserModel;
import com.weiqianghu.usedbook.model.inf.IEditUserModel;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.view.view.IEditUserView;

import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by weiqianghu on 2016/2/22.
 */
public class EditUserPresenter {
    private IEditUserModel mEditUserModel;
    private IEditUserView mEditUserView;
    private Handler handler;

    public EditUserPresenter(IEditUserView iEditUserView,Handler handler){
        this.mEditUserView=iEditUserView;
        this.handler=handler;
        mEditUserModel=new EditUserModel();
    }

    public void updateUser(Context context, UserBean userBean){
        UpdateListener updateListener=new UpdateListener() {
            @Override
            public void onSuccess() {
                Message message = new Message();
                message.what = Constant.SUCCESS;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(int i, String s) {
                Message message = new Message();
                message.what = Constant.FAILURE;

                FailureMessage failureMessage = new FailureMessage();
                failureMessage.setMsgCode(i);
                failureMessage.setMsg(s);

                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.FAILURE_MESSAGE, failureMessage);

                message.setData(bundle);
                handler.sendMessage(message);
            }
        };

        mEditUserModel.updateUser(context,updateListener,userBean);
    }
}
