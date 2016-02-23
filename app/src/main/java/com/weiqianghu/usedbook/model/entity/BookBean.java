package com.weiqianghu.usedbook.model.entity;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by 胡伟强 on 2016/1/26.
 */
public class BookBean extends BmobObject implements Serializable{
    private int price;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
