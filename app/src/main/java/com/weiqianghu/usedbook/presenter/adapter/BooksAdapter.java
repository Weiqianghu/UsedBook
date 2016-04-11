package com.weiqianghu.usedbook.presenter.adapter;

import android.content.Context;
import android.net.Uri;

import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.entity.BookBean;
import com.weiqianghu.usedbook.model.entity.BookImgsBean;
import com.weiqianghu.usedbook.model.entity.BookModel;
import com.weiqianghu.usedbook.view.ViewHolder;

import java.util.List;

/**
 * Created by weiqianghu on 2016/3/17.
 */
public class BooksAdapter extends CommonAdapter<BookModel> {
    public BooksAdapter(Context context, List<BookModel> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder helper, BookModel item) {
        BookModel bookModel = (BookModel) item;
        BookBean book = bookModel.getBook();
        List<BookImgsBean> bookImgs = bookModel.getBookImgs();

        helper.setText(R.id.tv_item_book_name, book.getBookName());
        helper.setText(R.id.tv_item_book_percent, book.getPercentDescribe());
        helper.setText(R.id.tv_item_book_price, book.getPrice() + "￥");
        helper.setText(R.id.tv_item_book_shop, book.getShop().getShopName());

        if(bookImgs.size()>0){
            Uri uri = Uri.parse(bookImgs.get(0).getImg());
            helper.setImageForSimpleDraweeViewUri(R.id.iv_item_book_img,uri);
        }else {
            Uri uri = Uri.parse("res://com.weiqianghu.usedbook_shop/" + R.mipmap.upload_img);
            helper.setImageForSimpleDraweeViewUri(R.id.iv_item_book_img, uri);
        }
    }
}
