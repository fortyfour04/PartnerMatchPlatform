package com.forty.huoban.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.forty.huoban.exception.BusinessException;
import com.forty.huoban.model.domain.Team;
import com.forty.huoban.model.dto.TeamQuery;
import com.forty.huoban.service.TeamService;
import com.forty.huoban.utils.Result;
import com.forty.huoban.utils.ResultCodeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    private TeamService teamService;

    @ApiOperation("新增队伍")
    @PostMapping("/add")
    public Result<Long> addTeam(@RequestBody Team team) {
        if (team == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR);
        }
        boolean res = teamService.save(team);
        if (!res) {
            throw new BusinessException(ResultCodeEnum.SYSTEM_ERROR,"插入失败");
        }
        System.out.println("Add Team Succeed");
        return Result.ok(team.getId());
    }

    @ApiOperation("删除队伍")
    @PostMapping("/delete")
    public Result<Boolean> deleteTeam(@RequestBody Long id) {
        if (id == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR);
        }
        boolean res = teamService.removeById(id);
        if (!res){
            throw new BusinessException(ResultCodeEnum.SYSTEM_ERROR,"删除失败");
        }
        System.out.println("Delete Team Succeed");
        return Result.ok(true);
    }

    @ApiOperation("修改队伍")
    @PostMapping("/update")
    public Result<Boolean> updateTeam(@RequestBody Team team) {
        if (team == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR);
        }
        boolean res = teamService.updateById(team);
        if (!res){
            throw new BusinessException(ResultCodeEnum.SYSTEM_ERROR,"更新失败");
        }
        return Result.ok(true);
    }

    @ApiOperation("根据队伍id查询单一队伍")
    @GetMapping("/get")
    public Result<Team> getTeamById(long id) {
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
    public Result<List<Team>> listTeam(TeamQuery teamQuery) {
        if (teamQuery == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR);
        }
        //将对应的query数据copy进第一个参数team中
        Team team = new Team();
        BeanUtils.copyProperties(team, teamQuery);
        QueryWrapper<Team> wrapper = new QueryWrapper<>(team);
        List<Team> list = teamService.list(wrapper);
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
        BeanUtils.copyProperties(team, teamQuery);
        QueryWrapper<Team> wrapper = new QueryWrapper<>(team);
        //加入对应分页参数后查询
        Page<Team> teamPage = new Page<>(teamQuery.getPageNum(),teamQuery.getPageSize());
        Page<Team> resPage = teamService.page(teamPage,wrapper);
        System.out.println("Query Team Succeed");
        return Result.ok(resPage);
    }
}
