package com.yohannes.app.dev.newsapp.models;

/**
 * Created by Yohannes on 31-Mar-20.
 */

public class NewsDetail {
    private int id;
    private String newsTitle;
    private String newsDetail;
    private String imageUrl;

    public NewsDetail(int id, String newsTitle, String newsDetail, String imageUrl) {
        this.id = id;
        this.newsTitle = newsTitle;
        this.newsDetail = newsDetail;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsDetail() {
        return newsDetail;
    }

    public void setNewsDetail(String newsDetail) {
        this.newsDetail = newsDetail;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
