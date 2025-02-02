package com.forty.huoban.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: FortyFour
 * @description: 用户登录请求类
 * @time: 2024/12/2 19:03
 * @version:
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    private String userAccount;

    private String userPassword;
}
