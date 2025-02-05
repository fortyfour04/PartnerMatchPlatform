package com.forty.huoban.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.forty.huoban.model.domain.Team;
import com.forty.huoban.model.domain.User;

/**
* @author 18140
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2025-02-04 14:38:22
*/
public interface TeamService extends IService<Team> {

    /**
     * 新增队伍
     * @param team , loginUser
     * @return Long
     */
    public Long addTeam(Team team, User loginUser);

}
