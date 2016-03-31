package com.weiqianghu.usedbook.presenter.adapter;

import android.content.Context;
import android.net.Uri;

import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.entity.CommentBean;
import com.weiqianghu.usedbook.model.entity.UserBean;
import com.weiqianghu.usedbook.view.ViewHolderForRecyclerView;

import java.util.List;

/**
 * Created by weiqianghu on 2016/3/31.
 */
public class CommentAdapter extends CommonAdapterForRecycleView<CommentBean> {
    private Context mContext;

    public CommentAdapter(List<CommentBean> datas, int itemLayoutId, Context context) {
        super(datas, itemLayoutId);
        this.mContext = context;
    }

    @Override
    public void convert(ViewHolderForRecyclerView helper, CommentBean item) {
        UserBean userBean = item.getUser();

        if (userBean.getImg() != null) {
            Uri uri = Uri.parse(userBean.getImg());
            helper.setImageForSimpleDraweeViewUri(R.id.iv_user_img, uri);
        } else {
            Uri uri = Uri.parse("res://com.weiqianghu.usedbook/" + R.mipmap.user_img);
            helper.setImageForSimpleDraweeViewUri(R.id.iv_user_img, uri);
        }

        helper.setText(R.id.tv_username, userBean.getUsername());
        helper.setRating(R.id.rb_grade, item.getGrade());
        helper.setText(R.id.tv_date, item.getUpdatedAt().substring(0, 10));
        helper.setEmojiText(R.id.tv_comment_content, item.getContent(), mContext);
    }
}
