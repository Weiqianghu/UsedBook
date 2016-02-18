package com.weiqianghu.usedbook.model.inf;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.weiqianghu.usedbook.model.entity.BookBean;

import java.util.List;

/**
 * Created by 胡伟强 on 2016/1/29.
 */
public interface IBooksDetailModel {

    List<View> loadBookImgs(Activity activity, BookBean book);

}
