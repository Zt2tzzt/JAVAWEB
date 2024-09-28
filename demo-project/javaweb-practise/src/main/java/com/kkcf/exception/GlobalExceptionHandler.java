package com.kkcf.exception;

import com.kkcf.pojo.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理类
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<String> ex(Exception ex) {
        ex.printStackTrace();
        return Result.error("服务器异常：" + ex.getMessage());
    }
}
