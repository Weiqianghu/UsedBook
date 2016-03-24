package com.weiqianghu.usedbook.presenter.adapter;

import android.net.Uri;

import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.entity.BookBean;
import com.weiqianghu.usedbook.model.entity.BookImgsBean;
import com.weiqianghu.usedbook.model.entity.ShoppingCartModel;
import com.weiqianghu.usedbook.view.ViewHolderForRecyclerView;

import java.util.List;

/**
 * Created by weiqianghu on 2016/3/24.
 */
public class OrderAdapter extends CommonAdapterForRecycleView<ShoppingCartModel> {
    private List<ShoppingCartModel> mData;

    public OrderAdapter(List<ShoppingCartModel> datas, int itemLayoutId) {
        super(datas, itemLayoutId);
        mDatas = datas;
    }

    @Override
    public void convert(ViewHolderForRecyclerView helper, ShoppingCartModel item) {
        BookBean book = item.getShoppingCartBean().getBook();
        List<BookImgsBean> bookImgs = item.getBookImgs();

        helper.setText(R.id.tv_book_name, book.getBookName());
        helper.setText(R.id.tv_book_author, book.getAuthor());
        helper.setText(R.id.tv_percent_describe, book.getPercentDescribe());
        helper.setText(R.id.tv_price, String.valueOf(book.getPrice()+"￥"));
        helper.setText(R.id.tv_amount, String.valueOf("X"+item.getShoppingCartBean().getNumber()));

        if (bookImgs != null && bookImgs.size() > 0) {
            Uri uri = Uri.parse(bookImgs.get(0).getImg());
            helper.setImageForSimpleDraweeViewUri(R.id.iv_book_img, uri);
        } else {
            Uri uri = Uri.parse("res://com.weiqianghu.usedbook/" + R.mipmap.upload_img);
            helper.setImageForSimpleDraweeViewUri(R.id.iv_book_img, uri);
        }
    }
}
