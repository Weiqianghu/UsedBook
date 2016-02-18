package com.weiqianghu.usedbook.model.entity;

/**
 * Created by 胡伟强 on 2016/2/1.
 */
public class ShoppingCartBean {
    private double price;
    private int number;
    private double subtotal;//小计

    private boolean isChecked;//不存储在数据库，只用来解决checkbox错乱问题


    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
