package com.forty.huoban.exception;

import com.forty.huoban.utils.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: FortyFour
 * @description: 全局异常处理类
 * @time: 2025/1/20 14:34
 * @version:
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public Result handleBusinessException(BusinessException ex) {
        return Result.build(null,ex.getCode(),ex.getMessage(),ex.getDescription());
    }

}
