package com.forty.huoban.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: FortyFour
 * @description: 用户退出队伍请求类
 * @time: 2025/2/11 14:08
 * @version:
 */
@Data
public class TeamQuitRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 队伍id
     */
    private Long teamId;
}
