package com.weiqianghu.usedbook.presenter.adapter;

import android.net.Uri;

import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.entity.MessageModel;
import com.weiqianghu.usedbook.view.ViewHolderForRecyclerView;

import java.util.List;


/**
 * Created by weiqianghu on 2016/4/3.
 */
public class MessageAdapter extends CommonAdapterForRecycleView<MessageModel> {


    public MessageAdapter(List<MessageModel> datas, int itemLayoutId) {
        super(datas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolderForRecyclerView helper, MessageModel item) {
        if (item.getImg() != null) {
            Uri uri = Uri.parse(item.getImg());
            helper.setImageForSimpleDraweeViewUri(R.id.iv_img, uri);
        } else {
            Uri uri = Uri.parse("res://com.weiqianghu.usedbook_shop/" + R.mipmap.upload_img);
            helper.setImageForSimpleDraweeViewUri(R.id.iv_img, uri);
        }
        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_date, item.getDate());
        helper.setText(R.id.tv_content, item.getMessageContent());
        if (item.getUnRead() > 0) {
            helper.setText(R.id.tv_recent_unread, String.valueOf(item.getUnRead()));
            helper.setViewVisible(R.id.tv_recent_unread, true);
        }else {
            helper.setViewVisible(R.id.tv_recent_unread, false);
        }
    }
}
