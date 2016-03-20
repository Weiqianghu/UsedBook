package com.weiqianghu.usedbook.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by weiqianghu on 2016/3/20.
 */
public class PreferModel implements Parcelable {
    private PreferBean prefer;
    private List<BookImgsBean> bookImgs;

    public PreferModel() {
    }

    protected PreferModel(Parcel in) {
        prefer = in.readParcelable(PreferBean.class.getClassLoader());
    }

    public static final Creator<PreferModel> CREATOR = new Creator<PreferModel>() {
        @Override
        public PreferModel createFromParcel(Parcel in) {
            return new PreferModel(in);
        }

        @Override
        public PreferModel[] newArray(int size) {
            return new PreferModel[size];
        }
    };

    public PreferBean getPrefer() {
        return prefer;
    }

    public void setPrefer(PreferBean prefer) {
        this.prefer = prefer;
    }

    public List<BookImgsBean> getBookImgs() {
        return bookImgs;
    }

    public void setBookImgs(List<BookImgsBean> bookImgs) {
        this.bookImgs = bookImgs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(prefer, flags);
    }
}
