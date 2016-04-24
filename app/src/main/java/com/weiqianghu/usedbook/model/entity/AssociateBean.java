package com.weiqianghu.usedbook.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by weiqianghu on 2016/4/24.
 */
public class AssociateBean extends BmobObject implements Parcelable {
    private List<String> baseBook;
    private List<String> associateBook;
    private double confidence;

    public AssociateBean() {
    }


    protected AssociateBean(Parcel in) {
        baseBook = in.createStringArrayList();
        associateBook = in.createStringArrayList();
        confidence = in.readDouble();
    }

    public static final Creator<AssociateBean> CREATOR = new Creator<AssociateBean>() {
        @Override
        public AssociateBean createFromParcel(Parcel in) {
            return new AssociateBean(in);
        }

        @Override
        public AssociateBean[] newArray(int size) {
            return new AssociateBean[size];
        }
    };

    public List<String> getBaseBook() {
        return baseBook;
    }

    public void setBaseBook(List<String> baseBook) {
        this.baseBook = baseBook;
    }

    public List<String> getAssociateBook() {
        return associateBook;
    }

    public void setAssociateBook(List<String> associateBook) {
        this.associateBook = associateBook;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(baseBook);
        dest.writeStringList(associateBook);
        dest.writeDouble(confidence);
    }
}
