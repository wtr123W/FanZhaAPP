package com.example.counterfraud.bean;

import java.io.Serializable;

/**
 * 评论
 */
public class Comment implements Serializable {
    private Integer id;
    private Integer articleId;//文章ID
    private Integer userId;//用户ID
    private String content;//内容
    private String date;//时间
    private Integer type;//文章类型

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Comment(Integer id, Integer articleId, Integer userId, String content, String date, Integer type) {
        this.id = id;
        this.articleId = articleId;
        this.userId = userId;
        this.content = content;
        this.date = date;
        this.type = type;
    }
}
