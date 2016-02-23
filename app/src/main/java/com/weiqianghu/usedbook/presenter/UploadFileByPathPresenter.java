package com.weiqianghu.usedbook.presenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.bmob.btp.callback.UploadListener;
import com.weiqianghu.usedbook.model.entity.FailureMessageModel;
import com.weiqianghu.usedbook.model.impl.UploadFileByPathModel;
import com.weiqianghu.usedbook.model.inf.IUploadFileByPathModel;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.view.view.IUploadFileByPathView;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by weiqianghu on 2016/2/23.
 */
public class UploadFileByPathPresenter {
    private IUploadFileByPathModel mUploadFileByPathModel;
    private IUploadFileByPathView mUploadFileByPathView;
    private Handler handler;

    public UploadFileByPathPresenter(IUploadFileByPathView iUploadFileByPathView, Handler handler){
        this.mUploadFileByPathView=iUploadFileByPathView;
        this.handler=handler;
        mUploadFileByPathModel=new UploadFileByPathModel();
    }

    public void uploadFileByPath(Context context,String path){
        UploadListener uploadListener=new UploadListener() {
            @Override
            public void onSuccess(String fileName,String url,BmobFile file) {
                Log.i("bmob","文件上传成功："+fileName+",可访问的文件地址："+file.getUrl());

                Message message = new Message();
                message.what = Constant.SUCCESS;

                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.FILE, file);

                message.setData(bundle);
                handler.sendMessage(message);

            }

            @Override
            public void onProgress(int progress) {
                Log.i("bmob","onProgress :"+progress);
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                Message message = new Message();
                message.what = Constant.FAILURE;

                FailureMessageModel failureMessageModel = new FailureMessageModel();
                failureMessageModel.setMsgCode(statuscode);
                failureMessageModel.setMsg(errormsg);

                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.FAILURE_MESSAGE, failureMessageModel);

                message.setData(bundle);
                handler.sendMessage(message);
            }
        };

        mUploadFileByPathModel.uploadImgByPath(context,uploadListener,path);
    }
}
