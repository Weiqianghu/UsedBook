package com.weiqianghu.usedbook.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.entity.UserBean;
import com.weiqianghu.usedbook.util.FileUtil;
import com.weiqianghu.usedbook.util.ImgUtil;
import com.weiqianghu.usedbook.view.common.BaseActivity;

import java.io.File;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //删除上传文件时生成的临时文件
        new Thread() {
            public void run() {
                ImgUtil.deleteAllTempFiles(new File(FileUtil.getCachePath() + "/tempImg/"));
            }
        }.start();

        Handler x = new Handler();
        x.postDelayed(new splashHandler(), 3000);
    }

    class splashHandler implements Runnable {

        public void run() {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
