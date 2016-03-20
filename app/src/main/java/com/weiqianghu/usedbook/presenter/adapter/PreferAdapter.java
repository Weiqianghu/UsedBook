package com.weiqianghu.usedbook.presenter.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.entity.BookBean;
import com.weiqianghu.usedbook.model.entity.BookImgsBean;
import com.weiqianghu.usedbook.model.entity.PreferModel;
import com.weiqianghu.usedbook.model.entity.ShopBean;
import com.weiqianghu.usedbook.view.ViewHolderForRecyclerView;

import java.util.List;

/**
 * Created by weiqianghu on 2016/3/20.
 */
public class PreferAdapter extends CommonAdapterForRecycleView<PreferModel> {

    private static final int VIEW_TYPE_BOOK = 11;
    private static final int VIEW_TYPE_SHOP = 12;
    private static final int VIEW_TYPE_ERROR = 13;

    public PreferAdapter(List<PreferModel> datas, int itemLayoutId) {
        super(datas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolderForRecyclerView helper, PreferModel item) {
        if (item.getPrefer().getBook() != null) {
            BookBean book = item.getPrefer().getBook();
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
        } else if (item.getPrefer().getShop() != null) {
            ShopBean shop = item.getPrefer().getShop();
            helper.setText(R.id.tv_shop_name, shop.getShopName());
            if (shop.getUpdatedAt() != null) {
                helper.setText(R.id.tv_data, shop.getUpdatedAt().substring(0, 10));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mDatas.get(position).getPrefer().getShop() != null) {
            return VIEW_TYPE_SHOP;
        } else if (mDatas.get(position).getPrefer().getBook() != null) {
            return VIEW_TYPE_BOOK;
        }
        return VIEW_TYPE_ERROR;
    }

    @Override
    public ViewHolderForRecyclerView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_BOOK) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prefer_book, parent, false);
            view.setOnClickListener(this);
        } else if (viewType == VIEW_TYPE_SHOP) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prefer_shop, parent, false);
            view.setOnClickListener(this);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.error, parent, false);
        }
        return ViewHolderForRecyclerView.get(view);
    }
}
