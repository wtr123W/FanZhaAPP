package com.example.counterfraud.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 科普类型（0:推荐 1:刷单返利诈骗 2:虚假投资理财诈骗 3:虚假网络贷款诈骗）
 */
public enum KnowledgeTypeEnum {
    A0 ("推荐", 0),
    A1 ("刷单返利诈骗", 1),
    A2 ("虚假投资理财诈骗", 2),
    A3 ("虚假网络贷款诈骗", 3),


    ;

    // 成员变量
    private String desc;
    private int code;

    // 构造方法
    private KnowledgeTypeEnum(String desc, Integer code) {
        this.desc = desc;
        this.code = code;
    }

    // 普通方法
    public static String getName(Integer code) {
        for (KnowledgeTypeEnum c : KnowledgeTypeEnum.values()) {
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
        for (KnowledgeTypeEnum statusEnum : KnowledgeTypeEnum.values()) {
            list.add(statusEnum.getDesc());
        }
        return list;
    }
    public static List<Integer> getCodeList() {
        List<Integer> list = new ArrayList<>();
        for (KnowledgeTypeEnum statusEnum : KnowledgeTypeEnum.values()) {
            list.add(statusEnum.getCode());
        }
        return list;
    }
}



