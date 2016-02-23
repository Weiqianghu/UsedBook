package com.weiqianghu.usedbook.model.impl;

import android.content.Context;

import com.bmob.BTPFileResponse;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;
import com.weiqianghu.usedbook.model.inf.IUploadFileByPathModel;

/**
 * Created by weiqianghu on 2016/2/23.
 */
public class UploadFileByPathModel implements IUploadFileByPathModel {
    @Override
    public boolean uploadImgByPath(Context context, UploadListener uploadListener, String path) {
        BTPFileResponse response = BmobProFile.getInstance(context).upload(path,uploadListener);
        return true;
    }
}
