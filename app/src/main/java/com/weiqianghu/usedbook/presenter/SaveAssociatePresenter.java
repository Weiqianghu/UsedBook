package com.weiqianghu.usedbook.presenter;

import android.content.Context;
import android.os.Handler;

import com.weiqianghu.usedbook.model.entity.AssociateBean;
import com.weiqianghu.usedbook.model.impl.BatchSaveModel;
import com.weiqianghu.usedbook.model.inf.IBatchSaveModel;

import java.util.List;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by weiqianghu on 2016/4/16.
 */
public class SaveAssociatePresenter extends CommonPresenter {
    private IBatchSaveModel<AssociateBean> mBatchSaveModel;

    public SaveAssociatePresenter(Handler handler) {
        super(handler);
        mBatchSaveModel = new BatchSaveModel();
    }

    public void batchSaveAssociate(Context context, List<AssociateBean> associateBeens) {
        SaveListener saveListener = new SaveListener() {
            @Override
            public void onSuccess() {
                handleSuccess();
            }

            @Override
            public void onFailure(int code, String msg) {
                handleFailureMessage(code, msg);
            }
        };
        mBatchSaveModel.batchSave(context, associateBeens, saveListener);
    }
}
