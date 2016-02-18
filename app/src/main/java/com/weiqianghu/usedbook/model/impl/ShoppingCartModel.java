package com.weiqianghu.usedbook.model.impl;

import com.weiqianghu.usedbook.model.entity.ShoppingCartBean;
import com.weiqianghu.usedbook.model.inf.IShoppingCartModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 胡伟强 on 2016/2/1.
 */
public class ShoppingCartModel implements IShoppingCartModel{

    @Override
    public List<ShoppingCartBean> showShoppingCart() {
        List<ShoppingCartBean> shoppingCartBeanList=new ArrayList<>();
        for(int i=0;i<10;i++){
            ShoppingCartBean sp=new ShoppingCartBean();
            sp.setNumber(1);
            sp.setPrice(i);
            sp.setChecked(false);
            sp.setSubtotal(i);
            shoppingCartBeanList.add(sp);
        }
        return shoppingCartBeanList;
    }
}
