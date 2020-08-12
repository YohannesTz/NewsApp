package com.yohannes.app.dev.newsapp.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Yohannes on 09-Mar-20.
 */

//82b07e7028564df59ad2cbd7e6b7a213

public class News implements Serializable {
    private int id;
    private String uploader;
    private String newsHeader;
    private String newsContent;
    private String detailImage;
    private String date;
    private int view;


    public News(int id, String uploader, String newsHeader, String newsContent, String detailImage, String date, int view) {
        this.id = id;
        this.uploader = uploader;
        this.newsHeader = newsHeader;
        this.newsContent = newsContent;
        this.detailImage = detailImage;
        this.date = date;
        this.view = view;
    }

    public News() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public String getNewsHeader() {
        return newsHeader;
    }

    public void setNewsHeader(String newsHeader) {
        this.newsHeader = newsHeader;
    }

    public String getNewsContent() {
        return newsContent;
    }

    public void setNewsContent(String newsContent) {
        this.newsContent = newsContent;
    }

    public String getDetailImage() {
        return detailImage;
    }

    public void setDetailImage(String detailImage) {
        this.detailImage = detailImage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    @Override
    public String toString() {
        return "News has id of " + id + " and uploader of " + uploader + " and NewsHeader " + newsHeader;
    }
}
