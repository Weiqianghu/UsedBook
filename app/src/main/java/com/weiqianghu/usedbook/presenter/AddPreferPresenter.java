package com.weiqianghu.usedbook.presenter;

import android.content.Context;
import android.os.Handler;

import com.weiqianghu.usedbook.model.entity.PreferBean;
import com.weiqianghu.usedbook.model.impl.SaveModel;
import com.weiqianghu.usedbook.model.inf.ISaveModel;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by weiqianghu on 2016/3/19.
 */
public class AddPreferPresenter extends CommonPresenter {
    private ISaveModel mSaveModel;

    public AddPreferPresenter(Handler handler) {
        super(handler);
        mSaveModel = new SaveModel();
    }

    public void addPrefer(Context context, PreferBean preferBean) {
        SaveListener saveListener = new SaveListener() {
            @Override
            public void onSuccess() {
                handleSuccess();
            }

            @Override
            public void onFailure(int i, String s) {
                handleFailureMessage(i, s);
            }
        };

        mSaveModel.save(context, saveListener, preferBean);
    }
}
