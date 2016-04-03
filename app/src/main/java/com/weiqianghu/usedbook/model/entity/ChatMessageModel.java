package com.weiqianghu.usedbook.model.entity;

import java.io.Serializable;

import cn.bmob.newim.bean.BmobIMMessage;

/**
 * Created by weiqianghu on 2016/4/2.
 */
public class ChatMessageModel implements Serializable {
    private String fromImg;
    private String toImg;

    private String type;

    private BmobIMMessage message;

    public BmobIMMessage getMessage() {
        return message;
    }

    public void setMessage(BmobIMMessage message) {
        this.message = message;
    }

    public String getFromImg() {
        return fromImg;
    }

    public void setFromImg(String fromImg) {
        this.fromImg = fromImg;
    }

    public String getToImg() {
        return toImg;
    }

    public void setToImg(String toImg) {
        this.toImg = toImg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
