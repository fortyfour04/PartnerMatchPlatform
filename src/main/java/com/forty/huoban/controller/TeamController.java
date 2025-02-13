package com.forty.huoban.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.forty.huoban.exception.BusinessException;
import com.forty.huoban.model.domain.Team;
import com.forty.huoban.model.domain.TeamUser;
import com.forty.huoban.model.domain.User;
import com.forty.huoban.model.dto.TeamQuery;
import com.forty.huoban.model.request.TeamAddRequest;
import com.forty.huoban.model.request.TeamJoinRequest;
import com.forty.huoban.model.request.TeamQuitRequest;
import com.forty.huoban.model.request.TeamUpdateRequest;
import com.forty.huoban.model.vo.TeamUserVo;
import com.forty.huoban.service.TeamService;
import com.forty.huoban.service.TeamUserService;
import com.forty.huoban.service.UserService;
import com.forty.huoban.utils.Result;
import com.forty.huoban.model.enums.ResultCodeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: FortyFour
 * @description:
 * @time: 2025/2/4 14:37
 * @version:
 */
@Api(tags = "队伍管理Controller")
@RestController
@CrossOrigin(origins ="http://localhost:3000" , allowCredentials = "true")
@RequestMapping("/team")
@Slf4j
public class TeamController {

    @Autowired
    private UserService userService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamUserService teamUserService;

    @ApiOperation("新增队伍")
    @PostMapping("/add")
    public Result<Long> addTeam(@RequestBody TeamAddRequest teamAddRequest, HttpServletRequest request) {
        if (teamAddRequest == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Team team = new Team();
        BeanUtils.copyProperties(teamAddRequest, team);
        long res = teamService.addTeam(team, loginUser);
        System.out.println("Add Team Succeed");
        return Result.ok(res);
    }

    @ApiOperation("修改队伍")
    @PostMapping("/update")
    public Result<Boolean> updateTeam(@RequestBody TeamUpdateRequest teamUpdateRequest, HttpServletRequest request) {
        if (teamUpdateRequest == null) {
            throw new BusinessException(ResultCodeEnum.NULL_ERROR,"请求参数为空");
        }
        User loginUser = userService.getLoginUser(request);
        boolean res = teamService.updateTeam(teamUpdateRequest,loginUser);
        if (!res){
            throw new BusinessException(ResultCodeEnum.SYSTEM_ERROR,"更新失败");
        }
        return Result.ok(true);
    }

    @ApiOperation("根据队伍id查询单一队伍")
    @GetMapping("/get")
    public Result<Team> getTeamById(Long id) {
        if (id <= 0){
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR);
        }
        Team res = teamService.getById(id);
        if (res == null){
            throw new BusinessException(ResultCodeEnum.NULL_ERROR);
        }
        return Result.ok(res);
    }

    @ApiOperation("根据对应信息查询队伍列表")
    @GetMapping("/list")
    public Result<List<TeamUserVo>> listTeam(TeamQuery teamQuery, HttpServletRequest request) {
        if (teamQuery == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR);
        }
        boolean isAdmin = userService.isAdmin(request);
        List<TeamUserVo> list = teamService.listTeam(teamQuery,isAdmin);
        System.out.println("Query Team Succeed");
        return Result.ok(list);
    }

    @ApiOperation("根据对应信息分页查询队伍列表")
    @GetMapping("/list/page")
    public Result<Page<Team>> listTeamPage(TeamQuery teamQuery) {
        if (teamQuery == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR);
        }
        //将对应的query数据copy进第一个参数team中
        Team team = new Team();
        BeanUtils.copyProperties(teamQuery, team);
        QueryWrapper<Team> wrapper = new QueryWrapper<>(team);
        //加入对应分页参数后查询
        Page<Team> teamPage = new Page<>(teamQuery.getPageNum(),teamQuery.getPageSize());
        Page<Team> resPage = teamService.page(teamPage,wrapper);
        System.out.println("Query Team Succeed");
        return Result.ok(resPage);
    }

    @ApiOperation("查询当前用户加入的队伍")
    @GetMapping("/list/my/join")
    public Result<List<TeamUserVo>> listMyJoinedTeam(TeamQuery teamQuery, HttpServletRequest request) {
        if (teamQuery == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        QueryWrapper<TeamUser> teamUserQueryWrapper = new QueryWrapper<>();
        teamUserQueryWrapper.eq("userId", loginUser.getId());
        List<TeamUser> list = teamUserService.list(teamUserQueryWrapper);
        ArrayList<TeamUserVo> teamUserVos = new ArrayList<>();
        BeanUtils.copyProperties(list,teamUserVos);
        return Result.ok(teamUserVos);
    }

    @ApiOperation("查询当前用户作为队长的队伍")
    @GetMapping("/list/my/create")
    public Result<List<TeamUserVo>> listMyLeadTeam(TeamQuery teamQuery, HttpServletRequest request) {
        if (teamQuery == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        teamQuery.setLeaderId(loginUser.getId());
        List<TeamUserVo> teamUserVoList = teamService.listTeam(teamQuery, true);
        return Result.ok(teamUserVoList);
    }

    @ApiOperation("用户加入队伍")
    @PostMapping("/join")
    public Result<Boolean> joinTeam(@RequestBody TeamJoinRequest teamJoinRequest, HttpServletRequest request) {
        if (teamJoinRequest == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean res = teamService.joinTeam(teamJoinRequest, loginUser);
        return Result.ok(res);
    }

    @ApiOperation("用户退出队伍")
    @PostMapping("/quit")
    public Result<Boolean> quitTeam(@RequestBody TeamQuitRequest teamQuitRequest, HttpServletRequest request) {
        if (teamQuitRequest == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean res = teamService.quitTeam(teamQuitRequest, loginUser);
        return Result.ok(res);
    }

    @ApiOperation("队长解散队伍")
    @PostMapping("/delete")
    public Result<Boolean> deleteTeam(@RequestBody Long teamId, HttpServletRequest request) {
        if (teamId <= 0) {
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean res = teamService.deleteTeam(teamId, loginUser);
        return Result.ok(res);
    }
}
