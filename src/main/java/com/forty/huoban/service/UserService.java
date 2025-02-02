package com.forty.huoban.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.forty.huoban.model.domain.User;
import com.forty.huoban.utils.Result;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author fortyfour
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-11-20 16:17:11
*/
@Service
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return Long
     */
    Long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     * @param userAccount
     * @param userPassword
     * @param request
     * @return User
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 更新用户信息
     * @param user
     * @param loginUser
     * @return
     */
    int userUpdate(User user, User loginUser);

    /**
     * 退出登录
     * @param request
     * @return int
     */
    int userLogout(HttpServletRequest request);

    /**
     * 根据标签查询用户
     *
     * @param tags
     * @return List<User>
     */
    List<User> searchByUserTags(List<String> tags);

    /**
     * 用户数据脱敏
     * @param user
     * @return User
     */
    public User getSafetyUser(User user);

    /**
     * 获取当前登录用户信息
     * @param request
     * @return User
     */
    public User getLoginUser(HttpServletRequest request);

    /**
     * 判断是否为管理员
     * @param user
     * @return Boolean
     */
    public Boolean isAdmin(User user);

    /**
     * 判断是否为管理员
     * @param request
     * @return Boolean
     */
    public Boolean isAdmin(HttpServletRequest request);
}
