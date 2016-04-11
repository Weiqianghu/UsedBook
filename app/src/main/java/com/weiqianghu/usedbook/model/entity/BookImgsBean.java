package com.weiqianghu.usedbook.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by weiqianghu on 2016/3/10.
 */
public class BookImgsBean extends BmobObject implements Parcelable, Serializable {
    private String img;
    private BookBean book;

    public BookImgsBean() {
    }

    protected BookImgsBean(Parcel in) {
        img = in.readString();
        book = in.readParcelable(BookBean.class.getClassLoader());
    }

    public static final Creator<BookImgsBean> CREATOR = new Creator<BookImgsBean>() {
        @Override
        public BookImgsBean createFromParcel(Parcel in) {
            return new BookImgsBean(in);
        }

        @Override
        public BookImgsBean[] newArray(int size) {
            return new BookImgsBean[size];
        }
    };

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public BookBean getBook() {
        return book;
    }

    public void setBook(BookBean book) {
        this.book = book;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(img);
        dest.writeParcelable(book, flags);
    }
}
