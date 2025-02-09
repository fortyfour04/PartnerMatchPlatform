package com.forty.huoban.model.request;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: FortyFour
 * @description: 队伍更新请求类
 * @time: 2025/2/9 14:48
 * @version:
 */
@Data
public class TeamUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 队伍id
     */
    private Long id;

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
     * 队伍过期时间
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
