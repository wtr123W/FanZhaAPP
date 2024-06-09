package com.example.counterfraud.bean;

import java.io.Serializable;

//报案
public class Report implements Serializable {
    private int id;
    private int userId;
    private String number;
    private String name;
    private String sex;
    private String phone;
    private String address;
    private String reportTime;
    private String crimeTime;
    private String crimeAddress;
    private String content;

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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public String getCrimeTime() {
        return crimeTime;
    }

    public void setCrimeTime(String crimeTime) {
        this.crimeTime = crimeTime;
    }

    public String getCrimeAddress() {
        return crimeAddress;
    }

    public void setCrimeAddress(String crimeAddress) {
        this.crimeAddress = crimeAddress;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Report(int id, int userId, String number, String name, String sex, String phone, String address, String reportTime, String crimeTime, String crimeAddress, String content) {
        this.id = id;
        this.userId = userId;
        this.number = number;
        this.name = name;
        this.sex = sex;
        this.phone = phone;
        this.address = address;
        this.reportTime = reportTime;
        this.crimeTime = crimeTime;
        this.crimeAddress = crimeAddress;
        this.content = content;
    }
}
