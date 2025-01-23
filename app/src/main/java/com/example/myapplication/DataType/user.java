package com.example.myapplication.DataType;

public class user {

    String uid, name, email, password, defaultImageUrl, description;
    Object friend;

    String[] news;


    public user() {
    }

    public Object getFriend() {
        return friend;
    }

    public void setFriend(Object friend) {
        this.friend = friend;
    }

    public user(String uid, String name, String email, String password, String defaultImageUrl, String description) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.password = password;
        this.defaultImageUrl = defaultImageUrl;
        this.description = description;
        this.friend = null;
        this.news = null;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDefaultImageUrl() {
        return defaultImageUrl;
    }

    public void setDefaultImageUrl(String defaultImageUrl) {
        this.defaultImageUrl = defaultImageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
