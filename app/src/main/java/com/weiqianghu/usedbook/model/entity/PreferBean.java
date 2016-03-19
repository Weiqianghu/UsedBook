package com.weiqianghu.usedbook.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import cn.bmob.v3.BmobObject;

/**
 * Created by weiqianghu on 2016/3/19.
 */
public class PreferBean extends BmobObject implements Parcelable {
    private UserBean user;
    private BookBean book;
    private ShopBean shop;
    private Boolean isDelete;

    public PreferBean() {
    }

    protected PreferBean(Parcel in) {
        book = in.readParcelable(BookBean.class.getClassLoader());
    }

    public static final Creator<PreferBean> CREATOR = new Creator<PreferBean>() {
        @Override
        public PreferBean createFromParcel(Parcel in) {
            return new PreferBean(in);
        }

        @Override
        public PreferBean[] newArray(int size) {
            return new PreferBean[size];
        }
    };

    public Boolean getDelete() {
        return isDelete;
    }

    public void setDelete(Boolean delete) {
        isDelete = delete;
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

    public ShopBean getShop() {
        return shop;
    }

    public void setShop(ShopBean shop) {
        this.shop = shop;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(book, flags);
    }
}
