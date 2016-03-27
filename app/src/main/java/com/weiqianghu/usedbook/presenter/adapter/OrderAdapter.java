package com.weiqianghu.usedbook.presenter.adapter;

import android.net.Uri;

import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.entity.BookBean;
import com.weiqianghu.usedbook.model.entity.BookImgsBean;
import com.weiqianghu.usedbook.model.entity.OrderBean;
import com.weiqianghu.usedbook.model.entity.OrderModel;
import com.weiqianghu.usedbook.util.OrderStateUtil;
import com.weiqianghu.usedbook.view.ViewHolderForRecyclerView;

import java.util.List;


/**
 * Created by weiqianghu on 2016/3/25.
 */
public class OrderAdapter extends CommonAdapterForRecycleView<OrderModel> {
    public OrderAdapter(List<OrderModel> mDatas, int itemLayoutId) {
        super(mDatas, itemLayoutId);
    }


    @Override
    public void convert(ViewHolderForRecyclerView helper, OrderModel item) {
        OrderBean order = item.getOrderBean();
        BookBean book = order.getBook();
        List<BookImgsBean> bookImgs = item.getBookImgs();

        helper.setText(R.id.tv_book_name, book.getBookName());
        helper.setText(R.id.tv_book_author, book.getAuthor());
        helper.setText(R.id.tv_percent_describe, book.getPercentDescribe());
        helper.setText(R.id.tv_price, String.valueOf(book.getPrice() + "￥"));
        helper.setText(R.id.tv_amount, String.valueOf("X" + order.getAmount()));
        helper.setText(R.id.tv_order_state, OrderStateUtil.getStrByOrderState(order.getOrderState()));

        if (bookImgs != null && bookImgs.size() > 0) {
            Uri uri = Uri.parse(bookImgs.get(0).getImg());
            helper.setImageForSimpleDraweeViewUri(R.id.iv_book_img, uri);
        } else {
            Uri uri = Uri.parse("res://com.weiqianghu.usedbook/" + R.mipmap.upload_img);
            helper.setImageForSimpleDraweeViewUri(R.id.iv_book_img, uri);
        }
    }
}
