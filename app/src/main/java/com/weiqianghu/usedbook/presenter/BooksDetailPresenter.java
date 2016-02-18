package com.weiqianghu.usedbook.presenter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.weiqianghu.usedbook.model.entity.BookBean;
import com.weiqianghu.usedbook.model.impl.BookDetailModel;
import com.weiqianghu.usedbook.model.inf.IBooksDetailModel;
import com.weiqianghu.usedbook.view.view.IBooksDetailView;

import java.util.List;

/**
 * Created by 胡伟强 on 2016/1/29.
 */
public class BooksDetailPresenter {
    private Activity mActivity;
    private IBooksDetailModel mBooksDetailModel;
    private IBooksDetailView mIBooksDetailView;

    private BookBean mBook;

    public BooksDetailPresenter(Activity activity,IBooksDetailView booksDetailView,BookBean book){
        this.mActivity=activity;
        this.mIBooksDetailView=booksDetailView;
        mBooksDetailModel=new BookDetailModel();
        this.mBook=book;
    }

    public void loadBookImgs(){
        List<View> views=mBooksDetailModel.loadBookImgs(mActivity,mBook);
        mIBooksDetailView.setBookImgs(views);
    }
}
