package com.weiqianghu.usedbook.model.inf;

import android.content.Context;

import com.weiqianghu.usedbook.model.entity.UserBean;

import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by weiqianghu on 2016/2/22.
 */
public interface IEditUserModel {
    boolean updateUser(Context context, UpdateListener updateListener, UserBean userBean);
}
