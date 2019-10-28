package com.egypt.supporting.supportingonline.Models;

public class ChatModel {
    private String text;
    private boolean isMe;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean me) {
        isMe = me;
    }
}
