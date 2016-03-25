package com.weiqianghu.usedbook.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import cn.bmob.v3.BmobObject;

/**
 * Created by 胡伟强 on 2016/2/1.
 */
public class ShoppingCartBean extends BmobObject implements Parcelable {
    private String objectIdStr;
    private String shopObjectId;
    private double price;
    private int number;
    private double subtotal;//小计
    private UserBean user;
    private BookBean book;
    private boolean isOrder;
    private ShopBean shop;


    protected ShoppingCartBean(Parcel in) {
        objectIdStr = in.readString();
        shopObjectId = in.readString();
        price = in.readDouble();
        number = in.readInt();
        subtotal = in.readDouble();
        book = in.readParcelable(BookBean.class.getClassLoader());
        isOrder = in.readByte() != 0;
        shop = in.readParcelable(ShopBean.class.getClassLoader());
        isChecked = in.readByte() != 0;
    }

    public static final Creator<ShoppingCartBean> CREATOR = new Creator<ShoppingCartBean>() {
        @Override
        public ShoppingCartBean createFromParcel(Parcel in) {
            return new ShoppingCartBean(in);
        }

        @Override
        public ShoppingCartBean[] newArray(int size) {
            return new ShoppingCartBean[size];
        }
    };

    public String getShopObjectId() {
        return shopObjectId;
    }

    public void setShopObjectId(String shopObjectId) {
        this.shopObjectId = shopObjectId;
    }


    public ShopBean getShop() {
        return shop;
    }

    public void setShop(ShopBean shop) {
        this.shop = shop;
    }

    private boolean isChecked;//不存储在数据库，只用来解决checkbox错乱问题

    public ShoppingCartBean() {
    }


    public String getObjectIdStr() {
        return objectIdStr;
    }

    public void setObjectIdStr(String objectIdStr) {
        this.objectIdStr = objectIdStr;
    }

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

    public boolean isOrder() {
        return isOrder;
    }

    public void setOrder(boolean order) {
        isOrder = order;
    }

    public double getSubtotal() {
        return (Math.round(subtotal * 10) / 10.0);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(objectIdStr);
        dest.writeString(shopObjectId);
        dest.writeDouble(price);
        dest.writeInt(number);
        dest.writeDouble(subtotal);
        dest.writeParcelable(book, flags);
        dest.writeByte((byte) (isOrder ? 1 : 0));
        dest.writeParcelable(shop, flags);
        dest.writeByte((byte) (isChecked ? 1 : 0));
    }
}
