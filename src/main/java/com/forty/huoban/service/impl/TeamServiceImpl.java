package com.forty.huoban.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.forty.huoban.model.domain.Team;
import com.forty.huoban.mapper.TeamMapper;
import com.forty.huoban.service.TeamService;
import org.springframework.stereotype.Service;

/**
* @author 18140
* @description 针对表【team(队伍)】的数据库操作Service实现
* @createDate 2025-02-04 14:38:22
*/
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService{

}




