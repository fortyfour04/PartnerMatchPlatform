package com.forty.huoban.model.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author: FortyFour
 * @description: 队伍用户表视图对象
 * @time: 2025/2/7 12:22
 * @version:
 */
public class TeamUserVo implements Serializable {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 队伍id
     */
    private Long teamId;

    /**
     * 队员id
     */
    private Long userId;

    /**
     * 加入队伍时间
     */
    private Date joinTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 入队用户列表
     */
    private List<UserVo> users;
}
