package com.weiqianghu.usedbook.presenter;

import com.weiqianghu.usedbook.model.entity.BookBean;
import com.weiqianghu.usedbook.model.impl.BooksModel;
import com.weiqianghu.usedbook.model.inf.IBooksModel;
import com.weiqianghu.usedbook.view.view.IBooksView;

import java.util.List;

/**
 * Created by 胡伟强 on 2016/1/26.
 */
public class BooksPresenter {
    private IBooksView mBooksView;
    private IBooksModel mBooksModel;

    public BooksPresenter(IBooksView view){
        this.mBooksView=view;
        mBooksModel=new BooksModel();
    }

    public void loadBooks(){
        List<BookBean> books=mBooksModel.loadBooks();
        mBooksView.showBooks(books);
    }
}
