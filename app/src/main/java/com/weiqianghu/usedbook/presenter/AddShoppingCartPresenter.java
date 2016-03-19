package com.weiqianghu.usedbook.presenter;

import android.content.Context;
import android.os.Handler;

import com.weiqianghu.usedbook.model.entity.ShoppingCartBean;
import com.weiqianghu.usedbook.model.impl.SaveModel;
import com.weiqianghu.usedbook.model.impl.ShoppingCartModel;
import com.weiqianghu.usedbook.model.inf.ISaveModel;
import com.weiqianghu.usedbook.model.inf.IShoppingCartModel;
import com.weiqianghu.usedbook.view.view.IShoppingCartView;

import java.util.List;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by 胡伟强 on 2016/2/1.
 */
public class AddShoppingCartPresenter extends CommonPresenter {

    private IShoppingCartModel mModel;

    private ISaveModel mSaveModel;

    public AddShoppingCartPresenter(Handler handler) {
        super(handler);
        mModel = new ShoppingCartModel();

        mSaveModel = new SaveModel();
    }

    public void addShoppingCart(Context context, ShoppingCartBean shoppingCartBean) {
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

        mSaveModel.save(context, saveListener, shoppingCartBean);
    }
}
