package com.yohannes.app.dev.newsapp.models;

import java.io.Serializable;

/**
 * Created by Yohannes on 03-Apr-20.
 */

public class SavedArticle implements Serializable {
    private int id;
    private String title;
    private String detail;

    public SavedArticle(int id, String title, String detail, byte[] image) {
        this.id = id;
        this.title = title;
        this.detail = detail;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public byte[] getImage() {
        return image;
    }

    private byte[] image;
}
