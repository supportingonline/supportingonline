package com.egypt.supporting.supportingonline.Models;

public class ChatHistroyModel {

    private String userId,userName,userImage,content,time;
    private boolean reade;

    public boolean isReade() {
        return reade;
    }

    public void setReade(boolean reade) {
        this.reade = reade;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
