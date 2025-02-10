package com.forty.huoban.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: FortyFour
 * @description: 用户加入队伍请求类
 * @time: 2025/2/9 16:33
 * @version:
 */
@Data
public class TeamJoinRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 队伍id
     */
    private Long teamId;

    /**
     * 队伍密码
     */
    private String password;
}
