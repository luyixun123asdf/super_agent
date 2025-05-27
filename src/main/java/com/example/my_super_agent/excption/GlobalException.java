package com.example.my_super_agent.excption;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 全局异常处理
 */

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e) {
        return "error";
    }

    public String handleException(RuntimeException e) {
        return "error";
    }

    public String handleException(Error e) {
        return "error";
    }

}
