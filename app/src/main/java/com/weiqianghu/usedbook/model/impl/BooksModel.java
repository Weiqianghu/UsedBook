package com.weiqianghu.usedbook.model.impl;

import com.weiqianghu.usedbook.model.entity.BookBean;
import com.weiqianghu.usedbook.model.inf.IBooksModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 胡伟强 on 2016/1/26.
 */
public class BooksModel implements IBooksModel {
    @Override
    public List<BookBean> loadBooks() {
        List<BookBean> books=new ArrayList<>();
        for(int i=0;i<100;i++){
            BookBean book=new BookBean();
            book.setPrice(i);

            books.add(book);
        }
        return books;
    }
}
