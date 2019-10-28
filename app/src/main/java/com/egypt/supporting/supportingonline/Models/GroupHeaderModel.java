package com.egypt.supporting.supportingonline.Models;

public class GroupHeaderModel {

    private String details,type,besideTitle,id;
    private int image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBesideTitle() {
        return besideTitle;
    }

    public void setBesideTitle(String besideTitle) {
        this.besideTitle = besideTitle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
