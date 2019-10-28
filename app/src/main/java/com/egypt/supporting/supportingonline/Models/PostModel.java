package com.egypt.supporting.supportingonline.Models;

public class PostModel {
    private String id,name,im_profile,text,image,date,count_comment,count_wow,user_id,type;
    private boolean is_wowed;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIm_profile() {
        return im_profile;
    }

    public void setIm_profile(String im_profile) {
        this.im_profile = im_profile;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCount_comment() {
        return count_comment;
    }

    public void setCount_comment(String count_comment) {
        this.count_comment = count_comment;
    }

    public String getCount_wow() {
        return count_wow;
    }

    public void setCount_wow(String count_wow) {
        this.count_wow = count_wow;
    }

    public boolean isIs_wowed() {
        return is_wowed;
    }

    public void setIs_wowed(boolean is_wowed) {
        this.is_wowed = is_wowed;
    }
}
