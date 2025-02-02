package com.forty.huoban.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.forty.huoban.exception.BusinessException;
import com.forty.huoban.model.domain.User;
import com.forty.huoban.model.request.UserLoginRequest;
import com.forty.huoban.model.request.UserRegisterRequest;
import com.forty.huoban.service.UserService;
import com.forty.huoban.utils.Result;
import com.forty.huoban.utils.ResultCodeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.forty.huoban.constant.UserConstant.USER_LOGIN_STATE;


/**
 * @author: FortyFour
 * @description:
 * @time: 2024/11/21 14:01
 * @version:
 */
@Api(tags = "用户管理Controller")
@RestController
@CrossOrigin(origins ="http://localhost:3000" , allowCredentials = "true")
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Resource
    private RedisTemplate redisTemplate;

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public Result<Long> register(@RequestBody UserRegisterRequest userRegisterRequest) {
        //校验请求是否为空
        if (userRegisterRequest == null) {
            return Result.build(null,ResultCodeEnum.PARAM_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        //校验参数内容是否为空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return Result.build(null,502,"请求参数为空","");
        }
        Long user_id = userService.userRegister(userAccount,userPassword,checkPassword);
        return Result.ok(user_id);
    }

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        //控制层校验是否为空
        if (userLoginRequest == null) {
            return Result.build(null, ResultCodeEnum.PARAM_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        //控制层校验是否为空
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return Result.build(null, ResultCodeEnum.PARAM_ERROR);
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return Result.ok(user);
    }

    @ApiOperation("获取当前用户界面")
    @GetMapping("/current")
    public Result<User> getCurrentUser(HttpServletRequest request) {
//        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
//        User currentUser = (User) userObj;
//        if (currentUser == null) {
//            throw new BusinessException(ResultCodeEnum.NOT_LOGIN);
//        }
        User currentUser = userService.getLoginUser(request);
        long userId = currentUser.getId();
        // TODO 校验用户是否合法
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return Result.ok(safetyUser);
    }

    //TODO:尚未开发完成
    @ApiOperation("根据用户名查询用户")
    @GetMapping("/search")
    public Result<List<User>> searchUser(HttpServletRequest request, String Username) {
        return Result.ok(null);
    }

    @ApiOperation("根据标签查询用户")
    @GetMapping("/search/tags")
    public Result<List<User>> searchUserByTags(@RequestParam(required = false) List<String> tagNameList) {
        //在controller再次进行校验是否为空 确保安全性
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR);
        }

        List<User> users = userService.searchByUserTags(tagNameList);

        return Result.ok(users);
    }

    @ApiOperation("主页用户推荐")
    @GetMapping("/recommend")
    public Result<Page<User>> recommendUsers(HttpServletRequest request,long pageSize, long pageNum) {
        User currentUser = userService.getLoginUser(request);

        //用redis查询来优化主页的查询性能
        //自定义redis键的名称，要注意不要与已有名称冲突
        String redisKey = String.format("forty:user:recommend:%s",currentUser.getId());
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Page<User> userPage = (Page<User>) valueOperations.get(redisKey);
        //缓存中有内容则直接读取
        if (userPage != null) {
            System.out.println("Recommend Query Succeed");
            return Result.ok(userPage);
        }
        //若无则查数据库存入内存
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        userPage = userService.page(new Page<>(pageNum,pageSize), queryWrapper);
        try {
            valueOperations.set(redisKey,userPage,30000, TimeUnit.MILLISECONDS);  //1w毫秒
        } catch (Exception e) {
            log.error("redis set key error",e);
        }
        return Result.ok(userPage);
    }

    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public Result<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR);
        }
        int result = userService.userLogout(request);
        return Result.ok(result);
    }

    @ApiOperation("修改用户")
    @PostMapping("/update")
    public Result<Integer> updateUser(@RequestBody User user, HttpServletRequest request) {
        //校验参数
        if (user == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        int result = userService.userUpdate(user,loginUser);
        return Result.ok(result);
    }

    @ApiOperation("删除用户")
    @PostMapping("/delete")
    public Result<Boolean> userDelete(@RequestBody Long id, HttpServletRequest request) {
        //校验参数
        if (id == null || id <= 0) {
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR);
        }
        //校验权限 判断是否为管理员
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ResultCodeEnum.NO_AUTH);
        }
        boolean bool = userService.removeById(id);

        return Result.ok(bool);
    }

}
