package com.forty.huoban.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.forty.huoban.constant.UserConstant;
import com.forty.huoban.exception.BusinessException;
import com.forty.huoban.mapper.UserMapper;
import com.forty.huoban.model.domain.User;
import com.forty.huoban.model.enums.ResultCodeEnum;
import com.forty.huoban.model.vo.UserVo;
import com.forty.huoban.service.UserService;
import com.forty.huoban.utils.AlgorithmUtils;
import com.forty.huoban.utils.Result;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.forty.huoban.constant.UserConstant.USER_LOGIN_STATE;
import static com.forty.huoban.model.enums.ResultCodeEnum.*;

/**
* @author fortyfour
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2024-11-20 16:17:11
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;


    /**
     * 盐值，用于加密
     */
    private static final String SALT = "forty";

    /**
     * 用户登录
     * @param userAccount
     * @param userPassword
     * @param request
     * @return
     */
    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1.校验
        if (StringUtils.isEmpty(userAccount) || StringUtils.isEmpty(userPassword)) {
            return null;
        }
        //账号规则
        if (userAccount.length() < 8 || userPassword.length() < 8) {
            return null;
        }
        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return null;
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        // 密码错误
        if (user == null) {
            System.out.println("user login failed, userAccount cannot find");
            return null;
        }
        // 3. 用户脱敏
        User safetyUser = getSafetyUser(user);
        // 4. 记录用户的登录态和相关信息
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        System.out.println("Login Succeed");
        return safetyUser;
    }

    /**
     * 更新用户信息
     * @param user
     * @param loginUser
     * @return int
     * @description 此处将管理员与用户的更新业务写在同一个方法中，如果较为复杂可以拆成两个方法
     */
    @Override
    public int userUpdate(User user, User loginUser) {
        //首先判断要改的对象数据id是否传入
        long userId = user.getId();
        if (userId <= 0){
            throw new BusinessException(NULL_ERROR);
        }

        //若当前用户是管理员,则进行更新
        //若当前用户非管理员,则只能修改自身的信息
        if(!isAdmin(loginUser) && userId != loginUser.getId()){
            throw new BusinessException(NO_AUTH);
        }
        User oldUser = userMapper.selectById(userId);
        if (oldUser == null){
            throw new BusinessException(PARAM_ERROR);
        }
        return userMapper.updateById(user);
    }

    /**
     * 退出登录
     * @param request
     * @return
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    /**
     * 根据标签查询用户 内存查询
     *
     * @param taglist
     * @return List<User>
     */
    @Override
    public List<User> searchByUserTags(List<String> taglist){
        if (CollectionUtils.isEmpty(taglist)){
            return null;
        }

//        方法一：先查询所有用户，再在内存中进行查询
//          查询所有
//        long start = System.currentTimeMillis(); //运行时间比较两者性能
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        List<User> users = userMapper.selectList(queryWrapper);

        //在内存中进行寻找
        Gson gson = new Gson();
        List<User> userList = users.stream().filter(user -> {
            String tagStr = user.getTags();
            //tags判空
            if (StringUtils.isEmpty(tagStr)){
                return false;
            }
            //用gson实现json转字符串序列化
            Set<String> TagNameList = gson.fromJson(tagStr, new TypeToken<Set<String>>(){}.getType());
            TagNameList = Optional.ofNullable(TagNameList).orElse(new HashSet<>()); //用Optional类来减少if语句以实现判空
            for (String tagName : taglist) {
                if (!TagNameList.contains(tagName)) {
                    return false;
                }
            }
            return true;
        }).map(this::getSafetyUser).collect(Collectors.toList());
//        System.out.println("memory query time :"+(System.currentTimeMillis() - start));
        System.out.println("Query Succeed");
        return userList;
    }


    /**
     * 用户注册
     */
    @Override
    public Long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1.校验
        if (StringUtils.isEmpty(userAccount) || StringUtils.isEmpty(userPassword) || StringUtils.isEmpty(checkPassword)) {
            throw new BusinessException(PARAM_ERROR,"参数为空");
        }
        //账号规则
        if (userAccount.length() < 8 || userPassword.length() < 8) {
            System.out.println("注册失败！账号密码长度不能小于8位");
            throw new BusinessException(PARAM_ERROR,"账号密码长度不能小于8位");
        }
        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            System.out.println("注册失败！账号不能包含特殊字符");
            throw new BusinessException(PARAM_ERROR,"账号不能包含特殊字符");
        }

        //校验密码不符
        if (!userPassword.equals(checkPassword)) {
//            throw new BusinessException("密码错误",503,"校验密码与原密码不一致");
            throw new BusinessException(PARAM_ERROR);
        }

        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 3. 账号检验
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        userMapper.selectCount(queryWrapper);
        if (userMapper.selectCount(queryWrapper) > 0) {
            System.out.println("注册失败！账号已被注册");
            throw new BusinessException(USER_ACCOUNT_USED,"账号已被注册");
        }

        //获取数据并脱敏
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        userMapper.insert(user);
        User safetyUser = getSafetyUser(user);
        System.out.println("Register Succeed");
        return safetyUser.getId();

    }


    /**
     * 根据标签查询用户 SQL模糊匹配查询
     *
     * @param taglist
     * @return List<User>
     */
    @Deprecated
    public Result searchByUserTagsBySQL(List<String> taglist){
        if (CollectionUtils.isEmpty(taglist)){
            return Result.build(null, PARAM_ERROR);
        }
        QueryWrapper<User> queryWrapper0 = new QueryWrapper<>();
        userMapper.selectList(queryWrapper0);

//        方法二：直接用DQL模糊匹配进行查询
        long start = System.currentTimeMillis(); //运行时间比较两者性能
        //拼接and查询实现标签查询
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        for (String tags : taglist) {
            queryWrapper = queryWrapper.like("tags", tags);
        }

        //使用baseMapper中的查询条件进行查询
        List<User> users = userMapper.selectList(queryWrapper);
        //用户数据脱敏
        List<User> userList = users.stream().map(this::getSafetyUser).collect(Collectors.toList());
        System.out.println("SQL query time :"+(System.currentTimeMillis() - start));
        return Result.ok(userList);
    }

    /**
     *  用户数据脱敏
     *
     */
    @Override
    public User getSafetyUser(User originalUser) {
        if (originalUser == null){
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originalUser.getId());
        safetyUser.setUsername(originalUser.getUsername());
        safetyUser.setUserAccount(originalUser.getUserAccount());
        safetyUser.setAvatarUrl(originalUser.getAvatarUrl());
        safetyUser.setProfile(originalUser.getProfile());
        safetyUser.setEmail(originalUser.getEmail());
        safetyUser.setPhone(originalUser.getPhone());
        safetyUser.setGender(originalUser.getGender());
        safetyUser.setUserRole(originalUser.getUserRole());
        safetyUser.setCreateTime(originalUser.getCreateTime());
        safetyUser.setUserStatus(originalUser.getUserStatus());
        safetyUser.setTags(originalUser.getTags());
        return safetyUser;
    }

    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        if (request == null){
           return null;
        }
        //获取当前用户信息
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ResultCodeEnum.NOT_LOGIN);
        }
        return currentUser;
    }

    /**
     * 判断是否为管理员
     * @param user
     * @return boolean
     */
    @Override
    public Boolean isAdmin(User user) {
        return user != null && user.getUserRole() == UserConstant.ADMIN_ROLE;
    }

    /**
     * 判断是否为管理员
     * @param request
     * @return boolean
     */
    @Override
    public Boolean isAdmin(HttpServletRequest request) {
        //获取当前用户信息
        Object objUser = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) objUser;
        return user != null && user.getUserRole() == UserConstant.ADMIN_ROLE;
    }

    /**
     * 根据标签相似度进行匹配推荐
     * @param num
     * @param loginUser
     */
    @Override
    public List<User> matchUser(long num, User loginUser) {
        List<User> userList = this.list();
        String loginUserTags = loginUser.getTags();
        //使用Gson将tags字段中的json字符串解析为String列表
        //使用SortedMap对距离向量进行排序
        Gson gson = new Gson();
        SortedMap<Integer, Long> sortedDistanceMap = new TreeMap<>();
        List<String> loginTagList = gson.fromJson(loginUserTags, new TypeToken<List<UserVo>>() {
        }.getType());
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            String userTags = user.getTags();
            if (userTags.isEmpty()) {
                continue;
            }
            List<String> userTagList = gson.fromJson(userTags, new TypeToken<List<UserVo>>() {
            }.getType());
            //利用匹配算法工具计算距离向量
            long Distance = AlgorithmUtils.minDistance(userTagList, loginTagList);
            sortedDistanceMap.put(i, Distance);
        }
        //找出距离向量最短的nums条数据
        List<Integer> minDistanceList = sortedDistanceMap.keySet().stream().limit(num).collect(Collectors.toList());
        return minDistanceList.stream().map(index -> {
            return getSafetyUser(userList.get(index));
        }).collect(Collectors.toList());
    }

}




