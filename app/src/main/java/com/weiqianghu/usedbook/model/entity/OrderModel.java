package com.weiqianghu.usedbook.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by weiqianghu on 2016/3/25.
 */
public class OrderModel implements Parcelable {
    private OrderBean orderBean;
    private List<BookImgsBean> bookImgs;


    public OrderModel() {
    }


    protected OrderModel(Parcel in) {
        orderBean = in.readParcelable(OrderBean.class.getClassLoader());
        bookImgs = in.createTypedArrayList(BookImgsBean.CREATOR);
    }

    public static final Creator<OrderModel> CREATOR = new Creator<OrderModel>() {
        @Override
        public OrderModel createFromParcel(Parcel in) {
            return new OrderModel(in);
        }

        @Override
        public OrderModel[] newArray(int size) {
            return new OrderModel[size];
        }
    };

    public OrderBean getOrderBean() {
        return orderBean;
    }

    public void setOrderBean(OrderBean orderBean) {
        this.orderBean = orderBean;
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
        dest.writeParcelable(orderBean, flags);
        dest.writeTypedList(bookImgs);
    }
}
