package com.weiqianghu.usedbook.presenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import com.weiqianghu.usedbook.model.entity.ShoppingCartBean;
import com.weiqianghu.usedbook.model.impl.UpdateModel;
import com.weiqianghu.usedbook.model.inf.IUpdateModel;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.view.view.IUpdateView;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by weiqianghu on 2016/2/24.
 */
public class UpdatePresenter<T extends BmobObject> extends CommonPresenter {
    private IUpdateModel mUpdateModel;
    private IUpdateView mUpdateView;


    public UpdatePresenter(IUpdateView iUpdateView, Handler handler) {
        super(handler);
        this.mUpdateView = iUpdateView;
        mUpdateModel = new UpdateModel();
    }

    public void update(Context context, T object, String objectId) {

        UpdateListener updateListener = new UpdateListener() {
            @Override
            public void onSuccess() {
                handleSuccess();
            }

            @Override
            public void onFailure(int i, String s) {
                handleFailureMessage(i, s);
            }
        };

        mUpdateModel.update(context, object, objectId, updateListener);
    }

    public void update(Context context, T object, String objectId, final ShoppingCartBean shoppingCartBean) {

        UpdateListener updateListener = new UpdateListener() {
            @Override
            public void onSuccess() {
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constant.SHOPPINGCART, shoppingCartBean);
                handleSuccess(bundle);
            }

            @Override
            public void onFailure(int i, String s) {
                handleFailureMessage(i, s);
            }
        };

        mUpdateModel.update(context, object, objectId, updateListener);
    }
}
