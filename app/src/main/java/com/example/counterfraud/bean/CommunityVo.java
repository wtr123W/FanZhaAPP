package com.example.counterfraud.bean;

import java.io.Serializable;

/**
 * 讨论
 */
public class CommunityVo implements Serializable {
    private Integer id;
    private Integer userId;//用户ID
    private String title;//标题
    private String img;//图片
    private String content;//内容
    private String date;//时间
    private String name;//姓名
    private String photo;//头像

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public CommunityVo(Integer id, Integer userId, String title, String img, String content, String date, String name, String photo) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.img = img;
        this.content = content;
        this.date = date;
        this.name = name;
        this.photo = photo;
    }
}
