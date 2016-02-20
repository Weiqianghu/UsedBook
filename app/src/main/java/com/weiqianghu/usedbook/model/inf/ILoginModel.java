package com.weiqianghu.usedbook.model.inf;

import android.content.Context;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by 胡伟强 on 2016/1/27.
 */
public interface ILoginModel {
    boolean login(Context context, SaveListener saveListener, String username, String password);
}
