package com.forty.huoban.model.dto;

import com.forty.huoban.utils.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author: FortyFour
 * @description: team查询数据传输对象
 * @time: 2025/2/4 16:55
 * @version:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TeamQuery extends PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 队伍id
     */
    private Long teamId;

    /**
     * 队伍id列表
     */
    private List<Long> idList;

    /**
     * 队长id
     */
    private Long leaderId;

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
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 搜索关键字
     */
    private String searchText;

}
