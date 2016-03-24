package com.weiqianghu.usedbook.presenter.adapter;

import android.net.Uri;


import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.entity.BookBean;
import com.weiqianghu.usedbook.model.entity.BookImgsBean;
import com.weiqianghu.usedbook.model.entity.BookModel;
import com.weiqianghu.usedbook.view.ViewHolderForRecyclerView;

import java.util.List;

/**
 * Created by weiqianghu on 2016/3/11.
 */
public class BookAdapter extends CommonAdapterForRecycleView {

    public BookAdapter(List datas, int itemLayoutId) {
        super(datas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolderForRecyclerView helper, Object item) {
        BookModel bookModel = (BookModel) item;
        BookBean book = bookModel.getBook();
        List<BookImgsBean> bookImgs = bookModel.getBookImgs();

        helper.setText(R.id.tv_book_name, book.getBookName());
        helper.setText(R.id.tv_book_price, String.valueOf(book.getPrice()));
        helper.setText(R.id.tv_percent_describe, book.getPercentDescribe());
        helper.setText(R.id.tv_book_stock, String.valueOf(book.getStock()));
        helper.setText(R.id.tv_sales_volume, String.valueOf(book.getSalesVolume()));

        if (bookImgs.size() > 0) {
            Uri uri = Uri.parse(bookImgs.get(0).getImg());
            helper.setImageForSimpleDraweeViewUri(R.id.iv_book, uri);
        } else {
            Uri uri = Uri.parse("res://com.weiqianghu.usedbook/" + R.mipmap.upload_img);
            helper.setImageForSimpleDraweeViewUri(R.id.iv_book, uri);
        }
    }
}
