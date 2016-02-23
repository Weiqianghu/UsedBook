package com.weiqianghu.usedbook.model.inf;

import android.content.Context;

import com.bmob.btp.callback.UploadListener;

/**
 * Created by weiqianghu on 2016/2/23.
 */
public interface IUploadFileByPathModel {
    boolean uploadImgByPath(Context context, UploadListener uploadListener,String path);
}
