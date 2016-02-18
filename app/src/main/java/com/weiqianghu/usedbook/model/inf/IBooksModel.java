package com.weiqianghu.usedbook.model.inf;

import com.weiqianghu.usedbook.model.entity.BookBean;

import java.util.List;

/**
 * Created by 胡伟强 on 2016/1/26.
 */
public interface IBooksModel {
    List<BookBean> loadBooks();
}
