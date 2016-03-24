package com.weiqianghu.usedbook.presenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.weiqianghu.usedbook.model.entity.FailureMessageModel;
import com.weiqianghu.usedbook.model.entity.ShoppingCartBean;
import com.weiqianghu.usedbook.model.impl.SaveModel;
import com.weiqianghu.usedbook.model.inf.ISaveModel;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.view.view.ISaveView;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by weiqianghu on 2016/2/23.
 */
public class SavePresenter extends CommonPresenter {
    private ISaveModel mSaveModel;
    private ISaveView mSaveView;

    public SavePresenter(ISaveView iSaveView, Handler handler) {
        super(handler);
        this.mSaveView = iSaveView;
        mSaveModel = new SaveModel();
    }

    public void save(Context context, BmobObject bean) {
        SaveListener saveListener = new SaveListener() {
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

                FailureMessageModel failureMessageModel = new FailureMessageModel();
                failureMessageModel.setMsgCode(i);
                failureMessageModel.setMsg(s);

                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.FAILURE_MESSAGE, failureMessageModel);

                message.setData(bundle);
                handler.sendMessage(message);
            }
        };

        mSaveModel.save(context, saveListener, bean);
    }

    public void save(Context context, BmobObject bean, final ShoppingCartBean shoppingCartBean) {
        SaveListener saveListener = new SaveListener() {
            @Override
            public void onSuccess() {
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constant.DATA, shoppingCartBean);
                handleSuccess(bundle);
            }

            @Override
            public void onFailure(int i, String s) {
                handleFailureMessage(i, s);
            }
        };

        mSaveModel.save(context, saveListener, bean);
    }
}
