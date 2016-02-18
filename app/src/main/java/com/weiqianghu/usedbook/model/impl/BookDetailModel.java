package com.weiqianghu.usedbook.model.impl;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.entity.BookBean;
import com.weiqianghu.usedbook.model.inf.IBooksDetailModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 胡伟强 on 2016/1/29.
 */
public class BookDetailModel implements IBooksDetailModel {

    @Override
    public List<View> loadBookImgs(Activity activity, BookBean book) {
        LayoutInflater inflater = activity.getLayoutInflater();
        List<View> views = new ArrayList<>(3);

        ImageView img1 = (ImageView) inflater.inflate(R.layout.item_book_detail_img, null);
        img1.setImageResource(R.mipmap.book1);
        views.add(img1);

        ImageView img2 = (ImageView) inflater.inflate(R.layout.item_book_detail_img, null);
        img2.setImageResource(R.mipmap.book2);
        views.add(img2);

        ImageView img3 = (ImageView) inflater.inflate(R.layout.item_book_detail_img, null);
        img3.setImageResource(R.mipmap.book3);
        views.add(img3);

        return views;
    }
}
