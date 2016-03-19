package com.weiqianghu.usedbook.model.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by weiqianghu on 2016/3/19.
 */
public class PreferBean extends BmobObject {
    private UserBean user;
    private BookBean book;
    private ShopBean shop;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public BookBean getBook() {
        return book;
    }

    public void setBook(BookBean book) {
        this.book = book;
    }

    public ShopBean getShop() {
        return shop;
    }

    public void setShop(ShopBean shop) {
        this.shop = shop;
    }
}
