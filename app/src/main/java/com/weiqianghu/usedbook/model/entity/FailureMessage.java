package com.weiqianghu.usedbook.model.entity;

import java.io.Serializable;

/**
 * Created by weiqianghu on 2016/2/20.
 */
public class FailureMessage implements Serializable {
    private int msgCode;
    private String msg;

    public int getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(int msgCode) {
        this.msgCode = msgCode;
    }

    public String getMsg() {
        switch (msgCode) {
            case 202:
                msg = "此用户名已经被注册";
                break;
            case 101:
                msg = "用户名或密码错误";
                break;
            case 207:
                msg="验证码错误";
                break;
            case 10010:
                msg="此手机号使用过于频繁，请稍后再试";
        }
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
