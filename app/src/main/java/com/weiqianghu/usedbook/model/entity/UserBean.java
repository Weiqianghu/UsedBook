package com.weiqianghu.usedbook.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import cn.bmob.v3.BmobUser;

/**
 * Created by weiqianghu on 2016/2/19.
 */
public class UserBean extends BmobUser implements Parcelable, Serializable {
    private int age;
    private boolean sex;
    private String sexStr;
    private String img;
    private String address;
    private Double longitude;
    private Double latitude;

    private boolean isShop;
    private ShopBean shop;

    public UserBean() {
    }

    protected UserBean(Parcel in) {
        age = in.readInt();
        sex = in.readByte() != 0;
        sexStr = in.readString();
        img = in.readString();
        address = in.readString();
        isShop = in.readByte() != 0;
        shop = in.readParcelable(ShopBean.class.getClassLoader());
    }

    public static final Creator<UserBean> CREATOR = new Creator<UserBean>() {
        @Override
        public UserBean createFromParcel(Parcel in) {
            return new UserBean(in);
        }

        @Override
        public UserBean[] newArray(int size) {
            return new UserBean[size];
        }
    };

    public boolean isShop() {
        return isShop;
    }

    public void setShop(boolean shop) {
        isShop = shop;
    }

    public ShopBean getShop() {
        return shop;
    }

    public void setShop(ShopBean shop) {
        this.shop = shop;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getSexStr() {
        if (sex == true) {
            this.sexStr = "男";
        } else {
            this.sexStr = "女";
        }
        return sexStr;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(age);
        dest.writeByte((byte) (sex ? 1 : 0));
        dest.writeString(sexStr);
        dest.writeString(img);
        dest.writeString(address);
        dest.writeByte((byte) (isShop ? 1 : 0));
        dest.writeParcelable(shop, flags);
    }
}
