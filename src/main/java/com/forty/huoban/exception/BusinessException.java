package com.forty.huoban.exception;

import com.forty.huoban.utils.ResultCodeEnum;

/**
 * @author: FortyFour
 * @description: 自定义异常处理类
 * @time: 2024/12/2 19:08
 * @version:
 */

public class BusinessException extends RuntimeException {

    private int code;

    private String message;

    private String description;

    public BusinessException(String message, int code, String message1, String description) {
        super(message);
        this.code = code;
        this.message = message1;
        this.description = description;
    }

    public BusinessException(ResultCodeEnum errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getMessage();
    }

    public BusinessException(ResultCodeEnum errorCode, String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}

