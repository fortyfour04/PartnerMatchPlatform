package com.forty.huoban.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.forty.huoban.model.domain.TeamUser;
import com.forty.huoban.exception.BusinessException;
import com.forty.huoban.mapper.TeamMapper;
import com.forty.huoban.model.domain.Team;
import com.forty.huoban.model.domain.User;
import com.forty.huoban.model.dto.TeamQuery;
import com.forty.huoban.model.enums.ResultCodeEnum;
import com.forty.huoban.model.enums.TeamStatusEnum;
import com.forty.huoban.model.request.TeamJoinRequest;
import com.forty.huoban.model.request.TeamUpdateRequest;
import com.forty.huoban.model.vo.TeamUserVo;
import com.forty.huoban.model.vo.UserVo;
import com.forty.huoban.service.TeamService;
import com.forty.huoban.service.TeamUserService;
import com.forty.huoban.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
* @author Fortyfour
* @description 针对表【team(队伍)】的数据库操作Service实现
* @createDate 2025-02-04 14:38:22
*/
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService{

    @Resource
    private TeamUserService teamUserService;

    @Resource
    private UserService userService;

    /**
     * 新增队伍
     * @param team, loginUser
     * @return teamId
     */
    @Override
    @Transactional(rollbackFor = Exception.class) //开启事务注解 保证数据一致
    public Long addTeam(Team team, User loginUser){
        //1.请求参数是否为空？
        if (team == null){
            throw new BusinessException(ResultCodeEnum.NULL_ERROR,"请求参数为空");
        }
        //2.是否登录，未登录不允许创建
        if (loginUser == null){
            throw new BusinessException(ResultCodeEnum.NOT_LOGIN,"尚未登录");
        }
        //3.校验信息
        //  最大人数 > 1 且 <= 20
        int maxNum = team.getMaxNum();
        if (maxNum < 1 || maxNum > 20){
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR,"队伍人数要求大于1小于等于20个字符");
        }
        //  队伍标题 <= 20
        String teamName = team.getTeamName();
        if (teamName == null || teamName.length() > 20){
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR,"队伍标题要求小于等于20个字符");
        }
        //  描述 <= 512
        String description = team.getDescription();
        if (description != null && description.length() > 512){
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR,"队伍介绍过长");
        }
        //  status 是否公开（int）不传则默认为 0（公开）
        int StatusCode = Optional.ofNullable(team.getStatus()).orElse(0);
        TeamStatusEnum teamStatusEnum = TeamStatusEnum.getTeamStatusEnumByCode(StatusCode);
        if (teamStatusEnum == null){
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR,"队伍状态输入错误");
        }
        //  如果 status 是加密状态，一定要有密码，且密码 <= 32
        String password = team.getPassword();
        if (TeamStatusEnum.ENCRYPTED.equals(teamStatusEnum)){
            if (password.isEmpty()){
                throw new BusinessException(ResultCodeEnum.NULL_ERROR,"密码尚未输入");
            } else if (password.length() > 32) {
                throw new BusinessException(ResultCodeEnum.PARAM_ERROR,"密码最多32位");
            }
        }
        //  超时时间 > 当前时间
        Date expireTime =team.getExpireTime();
        if (expireTime == null){
            throw new BusinessException(ResultCodeEnum.NULL_ERROR,"超时时间不能为空");
        }
        if (new Date().after(expireTime)){
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR,"超时时间错误");
        }
        //  校验用户最多创建 5 个队伍
        //  TODO: 存在Bug：用户可能同时创建多个队伍  加锁解决
        long creatorId = loginUser.getId(); //获取当前登录用户的id
        QueryWrapper<Team> wrapper = new QueryWrapper<>();
        wrapper.eq("userId",creatorId);
        long count = this.count(wrapper); //myBatisPlus 提供的Service增强 即BaseMapper中的selectCount
        if (count >= 5){
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR,"创建队伍数量已达上限");
        }
        //4.插入队伍信息到队伍表
        //  此处需确保数据一致性，即保证两个插入操作同步成功/同步失败 否则会出现数据完整性异常。可使用Transactional事务实现
        team.setId(null);
        team.setCreatorId(creatorId);
        boolean save = this.save(team); //插入后获取自增的teamId
        Long teamId = team.getId();
        if (!save || teamId == null ){
            throw new BusinessException(ResultCodeEnum.SYSTEM_ERROR,"创建队伍失败");
        }
        //5.插入创始人用户 => 队伍关系到关系表
        TeamUser teamUser = new TeamUser();
        teamUser.setUserId(creatorId);
        teamUser.setTeamId(teamId);
        teamUser.setJoinTime(new Date());
        teamUser.setCreateTime(new Date());
        save = teamUserService.save(teamUser);
        if (!save){
            throw new BusinessException(ResultCodeEnum.SYSTEM_ERROR,"创建队伍失败");
        }
        return teamId;
    }

    /**
     * 查询队伍列表
     * @param teamQuery
     * @return List<TeamUserVo>
     */
    @Override
    public List<TeamUserVo> listTeam(TeamQuery teamQuery, boolean isAdmin) {
        QueryWrapper<Team> wrapper = new QueryWrapper<>();
        // 组合查询条件
        if (teamQuery == null){
            throw new BusinessException(ResultCodeEnum.NULL_ERROR);
        }else {
            Long id = teamQuery.getTeamId();
            if (id != null && id > 0){
                wrapper.eq("id", id);
            }

            List<Long> idList = teamQuery.getIdList();
            if (CollectionUtils.isNotEmpty(idList)){
                wrapper.in("id", idList);
            }

            String searchText = teamQuery.getSearchText();
            if (StringUtils.isNotBlank(searchText)) {
                wrapper.and(qw -> qw.like("name", searchText).or().like("description", searchText));
            }

            Integer maxNum = teamQuery.getMaxNum();
            if (maxNum != null && maxNum > 0){
                wrapper.eq("max_num",maxNum);
            }

            Long userId = teamQuery.getUserId();
            if (userId != null && userId > 0){
                wrapper.eq("user_id",userId);
            }

            Integer status = teamQuery.getStatus();
            TeamStatusEnum statusEnum = TeamStatusEnum.getTeamStatusEnumByCode(status);
            //没设置则默认为私密
            if (statusEnum == null){
                statusEnum = TeamStatusEnum.PRIVATE;
            }
            //只有管理员才能查看加密还有非公开的房间
            if (!isAdmin && TeamStatusEnum.PRIVATE.equals(statusEnum)){
                throw new BusinessException(ResultCodeEnum.NO_AUTH,"无权限");
            }
            wrapper.eq("status",statusEnum.getCode());
        }
        // 不展示已过期的队伍
        // expireTime is null or expireTime > now()
        wrapper.and(qw -> qw.gt("expireTime", new Date()).or().isNull("expireTime"));
        List<Team> teamList = this.list(wrapper);
        if (CollectionUtils.isEmpty(teamList)) {
            return new ArrayList<>();
        }

        List<TeamUserVo> teamUserVoList = new ArrayList<>();
        // 关联查询创建人的用户信息
        for (Team team : teamList) {
            Long userId = team.getCreatorId();
            if (userId == null) {
                continue;
            }
            User user = userService.getById(userId);
            TeamUserVo teamUserVo = new TeamUserVo();
            BeanUtils.copyProperties(team, teamUserVo);
            // 脱敏用户信息
            if (user != null) {
                UserVo userVo = new UserVo();
                BeanUtils.copyProperties(user, userVo);
                teamUserVo.setCreator(userVo);
            }
            teamUserVoList.add(teamUserVo);
        }
        return teamUserVoList;
    }

    /**
     * @param teamUpdateRequest
     * @param loginUser
     * @return teamId
     */
    @Override
    public boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser) {
        //是否登录，未登录则不允许更新
        if (loginUser == null){
            throw new BusinessException(ResultCodeEnum.NOT_LOGIN,"尚未登录");
        }
        //参数校验
        Long teamId = teamUpdateRequest.getTeamId();
        if (teamId == null || teamId < 0){
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR,"参数错误");
        }
        //校验该用户是否存在
        Team oldTeam = this.getById(teamId);
        if (oldTeam == null){
            throw new BusinessException(ResultCodeEnum.NULL_ERROR,"没有查询到相关信息");
        }
        //只有队伍创始人或系统设置的管理员可以对队伍进行修改
        if (!Objects.equals(oldTeam.getCreatorId(), loginUser.getId()) && !userService.isAdmin(loginUser)){
            throw new BusinessException(ResultCodeEnum.NO_AUTH,"无权限修改!") ;
        }
        //如果队伍状态修改为加密，必须要有密码
        Integer statusCode = teamUpdateRequest.getStatus();
        TeamStatusEnum statusEnum = TeamStatusEnum.getTeamStatusEnumByCode(statusCode);
        String password = teamUpdateRequest.getPassword();
        if (statusEnum == TeamStatusEnum.PRIVATE && password.isEmpty()){
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR,"未设置密码");
        }
        //如果队伍状态修改为公开，则拒绝传输密码并将原密码清空
        if (statusEnum == TeamStatusEnum.PUBLIC && !password.isEmpty()){
            teamUpdateRequest.setPassword("");
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR,"无需设置密码");
        }
        Team team = new Team();
        BeanUtils.copyProperties(teamUpdateRequest, team);
        return this.updateById(team);
    }

    /**
     * 加入队伍
     * @param teamJoinRequest
     * @param loginUser
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser) {
        //是否登录，未登录则不允许更新
        if (loginUser == null){
            throw new BusinessException(ResultCodeEnum.NOT_LOGIN,"尚未登录");
        }
        //参数校验
        if (teamJoinRequest == null){
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR,"参数错误");
        }
        Long teamId = teamJoinRequest.getTeamId();
        if (teamId == null || teamId < 0){
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR,"参数错误");
        }
        Team team = this.getById(teamId);
        if (team == null){
            throw new BusinessException(ResultCodeEnum.NULL_ERROR,"队伍不存在");
        }
        Date expireTime = team.getExpireTime();
        if (expireTime != null && expireTime.before(new Date())){
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR,"队伍已过期");
        }

        //业务需求实现
        //用户最多加入5支队伍
        Long userId = loginUser.getId();
        QueryWrapper<TeamUser> teamUserQueryWrapper = new QueryWrapper<>();
        teamUserQueryWrapper.eq("user_id", userId);
        long hasJoinedTeamCount = teamUserService.count(teamUserQueryWrapper);
        if (hasJoinedTeamCount >= 5){
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR,"最多同时创建或加入5支队伍");
        }
        //不能重复加入已加入的队伍(包括自己创建的)
        teamUserQueryWrapper = new QueryWrapper<>();
        teamUserQueryWrapper.eq("team_id", teamId);
        teamUserQueryWrapper.eq("user_id", userId);
        long hasJoinedTeam = teamUserService.count(teamUserQueryWrapper);
        if (hasJoinedTeam > 0){
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR,"不能重复加入队伍");
        }
        //只能加入未满和未过期的队伍
        teamUserQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Team> teamWrapper = new QueryWrapper<>();
        teamUserQueryWrapper.eq("team_id", teamId);
        long teamMemberNum = teamUserService.count(teamUserQueryWrapper);
        if (teamMemberNum >= team.getMaxNum()){
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR,"该队伍人数已满");
        }

        //队伍权限校验需求实现
        Integer statusCode = team.getStatus();
        TeamStatusEnum statusEnum = TeamStatusEnum.getTeamStatusEnumByCode(statusCode);
        //禁止加入私有队伍
        if (statusEnum == TeamStatusEnum.PRIVATE){
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR,"禁止加入私密队伍");
        }
        //如果队伍状态修改为加密，校验密码是否正确
        String password = teamJoinRequest.getPassword();
        String checkPassword = team.getPassword();
        if (statusEnum == TeamStatusEnum.ENCRYPTED ){
            if (password.isEmpty() || !password.equals(checkPassword) ){
                throw new BusinessException(ResultCodeEnum.PASSWORD_ERROR,"密码错误");
            }
        }

        //校验完毕插入数据
        TeamUser teamUser = new TeamUser();
        BeanUtils.copyProperties(teamJoinRequest, teamUser);
        teamUser.setJoinTime(new Date());
        return teamUserService.save(teamUser);
    }


}




