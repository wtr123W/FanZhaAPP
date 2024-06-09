package com.example.counterfraud.bean;

import java.io.Serializable;

/**
 * 用户
 */
public class User implements Serializable {
    private Integer id;//用户ID
    private String account;//账号
    private String password;//密码
    private String name;//昵称
    private String sex;//性别
    private String phone;//手机号
    private String room;//寝室号
    private String photo;//头像
    private Integer isEnable;//是否开启拦截

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Integer isEnable) {
        this.isEnable = isEnable;
    }

    public User(Integer id, String account, String password, String name, String sex, String phone, String room, String photo, Integer isEnable) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.name = name;
        this.sex = sex;
        this.phone = phone;
        this.room = room;
        this.photo = photo;
        this.isEnable = isEnable;
    }
}
