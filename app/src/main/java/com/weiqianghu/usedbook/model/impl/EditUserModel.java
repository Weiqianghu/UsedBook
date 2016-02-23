package com.weiqianghu.usedbook.model.impl;

import android.content.Context;

import com.weiqianghu.usedbook.model.entity.UserBean;
import com.weiqianghu.usedbook.model.inf.IEditUserModel;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by weiqianghu on 2016/2/22.
 */
public class EditUserModel implements IEditUserModel {
    @Override
    public boolean updateUser(Context context, UpdateListener updateListener, UserBean userBean) {
        UserBean currentUser = BmobUser.getCurrentUser(context, UserBean.class);
        userBean.update(context, currentUser.getObjectId(), updateListener);
        return true;
    }
}
