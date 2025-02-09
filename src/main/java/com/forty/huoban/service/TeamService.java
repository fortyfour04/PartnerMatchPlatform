package com.forty.huoban.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.forty.huoban.model.domain.Team;
import com.forty.huoban.model.domain.User;
import com.forty.huoban.model.dto.TeamQuery;
import com.forty.huoban.model.request.TeamUpdateRequest;
import com.forty.huoban.model.vo.TeamUserVo;

import java.util.List;

/**
* @author 18140
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2025-02-04 14:38:22
*/
public interface TeamService extends IService<Team> {

    /**
     * 新增队伍
     * @param team , loginUser
     * @return teamId
     */
    Long addTeam(Team team, User loginUser);

    /**
     * 按要求查询队伍
     * @param teamQuery
     * @param isAdmin
     * @return List<TeamUserVo>
     */
    List<TeamUserVo> listTeam(TeamQuery teamQuery, boolean isAdmin);

    /**
     * 修改队伍
     *
     * @param teamUpdateRequest
     * @param loginUser
     * @return teamId
     */
    boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser);
}
