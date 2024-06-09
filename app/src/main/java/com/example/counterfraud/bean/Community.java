package com.example.counterfraud.bean;

import java.io.Serializable;

/**
 * 讨论
 */
public class Community implements Serializable {
    private Integer id;
    private Integer userId;//用户ID
    private String title;//标题
    private String img;//图片
    private String date;//时间
    private String content;//内容

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Community(Integer id, Integer userId, String title, String img, String date, String content) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.img = img;
        this.date = date;
        this.content = content;
    }
}
