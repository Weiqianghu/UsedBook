package com.weiqianghu.usedbook.presenter;

import com.weiqianghu.usedbook.model.entity.ShoppingCartBean;
import com.weiqianghu.usedbook.model.impl.ShoppingCartModel;
import com.weiqianghu.usedbook.model.inf.IShoppingCartModel;
import com.weiqianghu.usedbook.view.view.IShoppingCartView;

import java.util.List;

/**
 * Created by 胡伟强 on 2016/2/1.
 */
public class ShoppingCartPresenter {

    private IShoppingCartModel mModel;
    private IShoppingCartView mView;

    public ShoppingCartPresenter(IShoppingCartView view){
        mModel=new ShoppingCartModel();
        this.mView=view;
    }

    public void showShoppingCart(){
        List<ShoppingCartBean> spList=mModel.showShoppingCart();
        mView.showShoppingCart(spList);

    }
}
