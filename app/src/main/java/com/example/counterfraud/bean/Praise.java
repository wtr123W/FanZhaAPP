package com.example.counterfraud.bean;

/**
 * 点赞
 */
public class Praise {
    private Integer id;
    private Integer articleId;//文章ID
    private Integer userId;//用户ID

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


    public Praise(Integer id, Integer articleId, Integer userId) {
        this.id = id;
        this.articleId = articleId;
        this.userId = userId;
    }
}
