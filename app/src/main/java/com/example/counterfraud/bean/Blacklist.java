package com.example.counterfraud.bean;

import java.io.Serializable;

//黑名单
public class Blacklist implements Serializable {
    private int id;
    private int userId;
    private String phone;
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Blacklist(int id, int userId, String phone, int status) {
        this.id = id;
        this.userId = userId;
        this.phone = phone;
        this.status = status;
    }
}
