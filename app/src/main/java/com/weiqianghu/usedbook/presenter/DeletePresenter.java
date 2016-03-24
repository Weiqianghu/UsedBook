package com.weiqianghu.usedbook.presenter;

import android.content.Context;
import android.os.Handler;

import com.weiqianghu.usedbook.model.impl.DeelteModel;
import com.weiqianghu.usedbook.model.impl.UpdateModel;
import com.weiqianghu.usedbook.model.inf.IDeleteModel;
import com.weiqianghu.usedbook.model.inf.IUpdateModel;
import com.weiqianghu.usedbook.view.view.IDeleteView;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by weiqianghu on 2016/2/24.
 */
public class DeletePresenter<T extends BmobObject> extends CommonPresenter {
    private IDeleteModel mDeleteModel;
    private IDeleteView mDeleteView;
    private Handler handler;

    private IUpdateModel mUpdateModel;

    public DeletePresenter(IDeleteView iDeleteView, Handler handler) {
        super(handler);
        this.mDeleteView = iDeleteView;
        mDeleteModel = new DeelteModel();
        mUpdateModel = new UpdateModel();
    }

    public void delete(Context context, T object) {
        DeleteListener deleteListener = new DeleteListener() {
            @Override
            public void onSuccess() {
                handleSuccess();
            }

            @Override
            public void onFailure(int i, String s) {
                handleFailureMessage(i, s);
            }
        };

        object.delete(context, deleteListener);
    }

    public void delete(Context context, T object, String objectId) {

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

}
