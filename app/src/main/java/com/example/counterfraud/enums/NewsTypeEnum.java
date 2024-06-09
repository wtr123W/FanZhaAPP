package com.example.counterfraud.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态类型（0:推荐 1:各地动态 2:反诈知识 3:时事热点）
 */

public enum NewsTypeEnum {
    A0 ("推荐", 0),
    A1 ("各地动态", 1),
    A2 ("反诈知识", 2),
    A3 ("时事热点", 3),

    ;

    // 成员变量
    private String desc;
    private int code;

    // 构造方法
    private NewsTypeEnum(String desc, Integer code) {
        this.desc = desc;
        this.code = code;
    }

    // 普通方法
    public static String getName(Integer code) {
        for (NewsTypeEnum c : NewsTypeEnum.values()) {
            if (c.getCode() == code) {
                return c.desc;
            }
        }
        return null;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    /**
     * 获取列表
     * @return
     */
    public static List<String> getNameList() {
        List<String> list = new ArrayList<>();
        for (NewsTypeEnum statusEnum : NewsTypeEnum.values()) {
            list.add(statusEnum.getDesc());
        }
        return list;
    }
    public static List<Integer> getCodeList() {
        List<Integer> list = new ArrayList<>();
        for (NewsTypeEnum statusEnum : NewsTypeEnum.values()) {
            list.add(statusEnum.getCode());
        }
        return list;
    }
}



