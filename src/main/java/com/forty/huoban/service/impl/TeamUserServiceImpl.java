package com.forty.huoban.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.forty.huoban.model.domain.TeamUser;
import com.forty.huoban.service.TeamUserService;
import com.forty.huoban.mapper.TeamUserMapper;
import org.springframework.stereotype.Service;

/**
* @author Fortyfour
* @description 针对表【team_user(队伍成员关系)】的数据库操作Service实现
* @createDate 2025-02-05 20:49:38
*/
@Service
public class TeamUserServiceImpl extends ServiceImpl<TeamUserMapper, TeamUser>
    implements TeamUserService{

}




