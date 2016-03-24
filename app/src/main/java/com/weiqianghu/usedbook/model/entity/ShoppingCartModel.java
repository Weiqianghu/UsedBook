package com.weiqianghu.usedbook.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by weiqianghu on 2016/3/19.
 */
public class ShoppingCartModel implements Parcelable {

    private ShoppingCartBean shoppingCartBean;
    private List<BookImgsBean> bookImgs;

    public ShoppingCartModel() {
    }


    protected ShoppingCartModel(Parcel in) {
        shoppingCartBean = in.readParcelable(ShoppingCartBean.class.getClassLoader());
        bookImgs = in.createTypedArrayList(BookImgsBean.CREATOR);
    }

    public static final Creator<ShoppingCartModel> CREATOR = new Creator<ShoppingCartModel>() {
        @Override
        public ShoppingCartModel createFromParcel(Parcel in) {
            return new ShoppingCartModel(in);
        }

        @Override
        public ShoppingCartModel[] newArray(int size) {
            return new ShoppingCartModel[size];
        }
    };

    public ShoppingCartBean getShoppingCartBean() {
        return shoppingCartBean;
    }

    public void setShoppingCartBean(ShoppingCartBean shoppingCartBean) {
        this.shoppingCartBean = shoppingCartBean;
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
        dest.writeParcelable(shoppingCartBean, flags);
        dest.writeTypedList(bookImgs);
    }
}
