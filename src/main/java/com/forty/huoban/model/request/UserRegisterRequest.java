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

    private String userAccount;     //账号

    private String userPassword;    //密码

    private String checkPassword;   //校验密码

}