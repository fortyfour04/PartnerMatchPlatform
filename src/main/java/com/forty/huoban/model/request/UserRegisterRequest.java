package com.forty.huoban.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: FortyFour
 * @description: 用户注册(新增用户)请求类
 * @time: 2024/12/3 19:53
 * @version:
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 校验密码
     */
    private String checkPassword;

}