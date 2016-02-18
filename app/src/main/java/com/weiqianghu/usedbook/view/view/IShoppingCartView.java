package com.weiqianghu.usedbook.view.view;

import com.weiqianghu.usedbook.model.entity.ShoppingCartBean;

import java.util.List;

/**
 * Created by 胡伟强 on 2016/2/1.
 */
public interface IShoppingCartView {
    void showShoppingCart(List<ShoppingCartBean> shoppingCartBeanList);
}
