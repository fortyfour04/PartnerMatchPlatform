package com.forty.huoban.model.request;

import lombok.Data;

import java.util.Date;

/**
 * @author: FortyFour
 * @description: 队伍新增请求类
 * @time: 2025/2/5 21:46
 * @version:
 */
@Data
public class TeamAddRequest {

    /**
     * 队伍名称
     */
    private String teamName;

    /**
     * 队伍介绍
     */
    private String description;

    /**
     * 队伍最大人数
     */
    private Integer maxNum;

    /**
     * 队伍过期时间 注：MySQl时区会自动加东八区8h，需要在前端传来的数据格式中进行修改!!
     */
    private Date expireTime;

    /**
     * 队伍状态(0-公开 1-私有 2-加密)
     */
    private Integer status;

    /**
     * 队伍密码
     */
    private String password;
}
