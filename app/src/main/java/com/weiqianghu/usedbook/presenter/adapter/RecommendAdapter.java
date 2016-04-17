package com.weiqianghu.usedbook.presenter.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.entity.BookBean;
import com.weiqianghu.usedbook.model.entity.BookImgsBean;
import com.weiqianghu.usedbook.model.entity.BookModel;
import com.weiqianghu.usedbook.model.entity.PreferModel;
import com.weiqianghu.usedbook.model.entity.RecommendBean;
import com.weiqianghu.usedbook.model.entity.ShopBean;
import com.weiqianghu.usedbook.view.ViewHolderForRecyclerView;

import java.util.List;

/**
 * Created by weiqianghu on 2016/3/20.
 */
public class RecommendAdapter extends CommonAdapterForRecycleView<BookModel> {

    private static final int VIEW_TYPE_BOOK = 11;
    private static final int VIEW_TYPE_SHOP = 12;
    private static final int VIEW_TYPE_ERROR = 13;

    public RecommendAdapter(List<BookModel> datas, int itemLayoutId) {
        super(datas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolderForRecyclerView helper, BookModel item) {
        if (item.getBook() != null) {
            BookBean book = item.getBook();
            List<BookImgsBean> bookImgs = item.getBookImgs();

            if (bookImgs.size() > 0) {
                Uri uri = Uri.parse(bookImgs.get(0).getImg());
                helper.setImageForSimpleDraweeViewUri(R.id.iv_book_img, uri);
            } else {
                Uri uri = Uri.parse("res://com.weiqianghu.usedbook_shop/" + R.mipmap.upload_img);
                helper.setImageForSimpleDraweeViewUri(R.id.iv_book_img, uri);
            }

            helper.setText(R.id.tv_book_name, book.getBookName());
            helper.setText(R.id.tv_book_price, book.getPrice() + "￥");
        }
    }
}
