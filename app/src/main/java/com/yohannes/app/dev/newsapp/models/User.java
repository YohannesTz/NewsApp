package com.yohannes.app.dev.newsapp.models;

import android.util.Log;

/**
 * Created by Yohannes on 21-Mar-20.
 */

public class User {
    private int id;
    private String username;
    private String password;
    private String name;
    private String fname;
    private int phonenum;
    private String bio;
    private String avatar_link;

    public User(int id, String username, String password, String name, String fname, int phonenum, String bio, String avatar_link) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.fname = fname;
        this.phonenum = phonenum;
        this.bio = bio;
        this.avatar_link = avatar_link;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public int getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(int phonenum) {
        this.phonenum = phonenum;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAvatar_link() {
        return avatar_link;
    }

    public void setAvatar_link(String avatar_link) {
        this.avatar_link = avatar_link;
    }

    @Override
    public String toString() {
        return String.format("User object id=%s username=%s password=%s name=%s fame=%s phonenum=%s bio=%s avatar_link=%s", id, username, password, name, fname, phonenum, bio, avatar_link);
    }
}
