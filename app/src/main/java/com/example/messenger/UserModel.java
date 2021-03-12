package com.example.messenger;

public class UserModel {

    String userid, imageUrl, username, status;


    public UserModel() {
    }


    public UserModel(String userid, String imageUrl, String username, String status) {
        this.userid = userid;
        this.imageUrl = imageUrl;
        this.username = username;
        this.status = status;
    }


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
