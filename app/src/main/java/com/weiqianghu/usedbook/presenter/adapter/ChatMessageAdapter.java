package com.weiqianghu.usedbook.presenter.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.entity.ChatMessageModel;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.util.DateUtil;
import com.weiqianghu.usedbook.view.ViewHolderForRecyclerView;

import java.util.List;

/**
 * Created by weiqianghu on 2016/4/2.
 */
public class ChatMessageAdapter extends CommonAdapterForRecycleView<ChatMessageModel> {
    private static final int MESSAGE_RECEIVE = 11;
    private static final int MESSAGE_SEND = 12;

    private Context mContext;

    public ChatMessageAdapter(List<ChatMessageModel> datas, int itemLayoutId, Context context) {
        super(datas, itemLayoutId);
        this.mContext = context;
    }

    @Override
    public void convert(ViewHolderForRecyclerView helper, ChatMessageModel item) {
        if (item.getType().equals(Constant.MESSAGE_RECEIVE)) {
            Uri uri = Uri.parse(item.getFromImg());
            helper.setImageForSimpleDraweeViewUri(R.id.iv_user_img, uri);
        } else {
            Uri uri = Uri.parse(item.getToImg());
            helper.setImageForSimpleDraweeViewUri(R.id.iv_user_img, uri);
        }

        helper.setText(R.id.tv_send_time, DateUtil.getChatTime(item.getMessage().getCreateTime()));
        //helper.setText(R.id.tv_chat_content, item.getContent());
        helper.setEmojiText(R.id.tv_chat_content, item.getMessage().getContent(), mContext);
    }

    @Override
    public int getItemViewType(int position) {
        if (mDatas.get(position).getType().equals(Constant.MESSAGE_RECEIVE)) {
            return MESSAGE_RECEIVE;
        } else {
            return MESSAGE_SEND;
        }
    }

    @Override
    public ViewHolderForRecyclerView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == MESSAGE_RECEIVE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_left_msg_text, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_right_msg_text, parent, false);
        }
        return ViewHolderForRecyclerView.get(view);
    }
}
