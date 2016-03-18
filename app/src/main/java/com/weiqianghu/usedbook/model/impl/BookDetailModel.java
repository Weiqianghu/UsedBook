package com.weiqianghu.usedbook.model.impl;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.entity.BookBean;
import com.weiqianghu.usedbook.model.entity.BookImgsBean;
import com.weiqianghu.usedbook.model.inf.IBooksDetailModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 胡伟强 on 2016/1/29.
 */
public class BookDetailModel implements IBooksDetailModel {

    @Override
    public List<View> loadBookImgs(Activity activity, List<BookImgsBean> imgs) {
        LayoutInflater inflater = activity.getLayoutInflater();
        List<View> views = new ArrayList<>(3);

        if (imgs != null) {
            for (int i = 0, length = imgs.size(); i < length; i++) {
                SimpleDraweeView img = (SimpleDraweeView) inflater.inflate(R.layout.item_book_detail_img, null);
                Uri uri = Uri.parse(imgs.get(i).getImg());
                img.setImageURI(uri);
                views.add(img);
            }
        }

        for (int i = 0, length = views.size(); i < 3 - length; i++) {
            SimpleDraweeView img = (SimpleDraweeView) inflater.inflate(R.layout.item_book_detail_img, null);
            Uri uri = Uri.parse("res://com.weiqianghu.usedbook_shop/" + R.mipmap.upload_img);
            img.setImageURI(uri);
            views.add(img);
        }

        return views;
    }
}
