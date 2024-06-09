package com.example.counterfraud.bean;

import java.io.Serializable;


public class Video implements Serializable {
    private int id;
    private String title;//标题
    private String coverUrl;//封面地址
    private String playUrl;//视频地址

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public Video(int id, String title, String coverUrl, String playUrl) {
        this.id = id;
        this.title = title;
        this.coverUrl = coverUrl;
        this.playUrl = playUrl;
    }
}
