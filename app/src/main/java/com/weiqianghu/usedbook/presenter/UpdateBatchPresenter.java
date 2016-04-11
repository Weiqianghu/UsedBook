package com.weiqianghu.usedbook.presenter;

import android.content.Context;
import android.os.Handler;

import com.weiqianghu.usedbook.model.impl.UpdateBatchModel;
import com.weiqianghu.usedbook.model.inf.IUpdateBatchModel;
import com.weiqianghu.usedbook.view.view.IUpdateBatchView;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by weiqianghu on 2016/2/24.
 */
public class UpdateBatchPresenter<T extends BmobObject> extends CommonPresenter{
    private IUpdateBatchModel mUpdateBatchModel;
    private IUpdateBatchView mUpdateBatchView;

    public UpdateBatchPresenter(IUpdateBatchView iUpdateBatchView, Handler handler){
        super(handler);

        this.mUpdateBatchView= iUpdateBatchView;
        this.handler=handler;
        mUpdateBatchModel=new UpdateBatchModel();
    }

    public void resetAddress(Context context,List<T> objects){
        UpdateListener updateListener=new UpdateListener() {
            @Override
            public void onSuccess() {
               handleSuccess();
            }

            @Override
            public void onFailure(int i, String s) {
                handleFailureMessage(i,s);
            }
        };

       mUpdateBatchModel.updateBatch(context,objects,updateListener);
    }
}
