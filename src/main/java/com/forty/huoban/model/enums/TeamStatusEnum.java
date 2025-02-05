package com.forty.huoban.model.enums;

import lombok.Getter;

/**
 * @author: FortyFour
 * @description: 队伍状态类型
 * @time: 2025/2/5 19:37
 * @version:
 */
public enum TeamStatusEnum {

    PUBLIC(0,"公开"),
    PRIVATE(1,"私有"),
    ENCRYPTED(2,"加密");

    private int code;

    private String desc;

    public static TeamStatusEnum getTeamStatusEnumByCode(Integer code) {
        if (code == null) return null;
        for (TeamStatusEnum teamStatusEnum : TeamStatusEnum.values()) {
            if (teamStatusEnum.getCode() == code){
                return teamStatusEnum;
            }
        }
        return null;
    }

    TeamStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
