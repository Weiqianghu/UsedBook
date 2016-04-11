package com.weiqianghu.usedbook.model.chat_model;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.weiqianghu.usedbook.model.chat_model.messagehandler.*;

import cn.bmob.newim.BmobIM;
import cn.bmob.push.BmobPush;
import cn.bmob.v3.BmobInstallation;

/**
 * @author :smile
 * @project:BmobIMApplication
 * @date :2016-01-13-10:19
 */
public class BmobIMApplication extends Application {

    private static BmobIMApplication INSTANCE;

    public static BmobIMApplication INSTANCE() {
        return INSTANCE;
    }

    private void setInstance(BmobIMApplication app) {
        setBmobIMApplication(app);
    }

    private static void setBmobIMApplication(BmobIMApplication a) {
        BmobIMApplication.INSTANCE = a;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);
        Fresco.initialize(this);

        //im初始化
        BmobIM.init(this);
        //注册消息接收器
        BmobIM.registerDefaultMessageHandler(new ChatMessageHandler(this));

        BmobInstallation.getCurrentInstallation(this).save();
        // 启动推送服务
        BmobPush.startWork(this);

    }
}
