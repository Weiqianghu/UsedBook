package com.weiqianghu.usedbook.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import cn.bmob.newim.bean.BmobIMConversation;

/**
 * Created by weiqianghu on 2016/4/3.
 */
public class MessageModel implements Parcelable {
    private String img;
    private String title;
    private String messageContent;
    private String date;
    private long unRead;
    private UserBean chatUser;

    private BmobIMConversation conversation;

    public MessageModel() {
    }


    protected MessageModel(Parcel in) {
        img = in.readString();
        title = in.readString();
        messageContent = in.readString();
        date = in.readString();
        unRead = in.readLong();
        chatUser = in.readParcelable(UserBean.class.getClassLoader());
    }

    public static final Creator<MessageModel> CREATOR = new Creator<MessageModel>() {
        @Override
        public MessageModel createFromParcel(Parcel in) {
            return new MessageModel(in);
        }

        @Override
        public MessageModel[] newArray(int size) {
            return new MessageModel[size];
        }
    };

    public UserBean getChatUser() {
        return chatUser;
    }

    public void setChatUser(UserBean chatUser) {
        this.chatUser = chatUser;
    }

    public long getUnRead() {
        return unRead;
    }

    public void setUnRead(long unRead) {
        this.unRead = unRead;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public BmobIMConversation getConversation() {
        return conversation;
    }

    public void setConversation(BmobIMConversation conversation) {
        this.conversation = conversation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(img);
        dest.writeString(title);
        dest.writeString(messageContent);
        dest.writeString(date);
        dest.writeLong(unRead);
        dest.writeParcelable(chatUser, flags);
    }
}
