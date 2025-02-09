package com.forty.huoban.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: FortyFour
 * @description: 用户视图对象
 * @time: 2025/2/7 12:21
 * @version:
 */
@Data
public class UserVo implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String username;

    /**
     *
     */
    private String userAccount;

    /**
     *
     */
    private String avatarUrl;

    /**
     *
     */
    private String profile;

    /**
     *
     */
    private Integer gender;

    /**
     *
     */
    private String phone;

    /**
     *
     */
    private String email;

    /**
     * 状态0-正常
     */
    private Integer userStatus;

    /**
     * 用户角色 0-普通用户 1-管理员
     */
    private Integer userRole;

    /**
     * 标签列表
     */
    private String tags;
}
