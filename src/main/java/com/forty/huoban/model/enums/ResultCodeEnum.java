package com.forty.huoban.model.enums;

/**
 * @author: FortyFour
 * @description: 统一返回结果状态信息类
 * @time: 2024/10/6 23:22
 * @version:
 */
public enum ResultCodeEnum {

    SUCCESS(200,"访问成功"),
    USERNAME_ERROR(501,"用户名错误"),
    PARAM_ERROR(502,"请求参数错误"),
    PASSWORD_ERROR(503,"密码错误"),
    NOT_LOGIN(504,"尚未登录"),
    USER_ACCOUNT_USED(505,"账户名已被注册"),
    NO_AUTH(506,"无权限"),
    NULL_ERROR(507,"空值错误"),
    SYSTEM_ERROR(508,"系统错误");

    private Integer code;
    private String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
