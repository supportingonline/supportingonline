package com.egypt.supporting.supportingonline.Models;

public class GroupModel {
    private String id,image,name,payment,receiveImage,userGroup;
    private boolean hasChildern,visable,hasdata,receive,delete;

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public String getReceiveImage() {
        return receiveImage;
    }

    public void setReceiveImage(String receiveImage) {
        this.receiveImage = receiveImage;
    }

    public boolean isReceive() {
        return receive;
    }

    public void setReceive(boolean receive) {
        this.receive = receive;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public boolean isHasdata() {
        return hasdata;
    }

    public void setHasdata(boolean hasdata) {
        this.hasdata = hasdata;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public boolean isVisable() {
        return visable;
    }

    public void setVisable(boolean visable) {
        this.visable = visable;
    }

    public boolean isHasChildern() {
        return hasChildern;
    }

    public void setHasChildern(boolean hasChildern) {
        this.hasChildern = hasChildern;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
