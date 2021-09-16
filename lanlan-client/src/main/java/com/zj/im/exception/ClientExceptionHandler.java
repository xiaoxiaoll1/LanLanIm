package com.zj.im.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.concurrent.ExecutionException;

/**
 * @author xiaozj
 */
@ControllerAdvice
@Slf4j
public class ClientExceptionHandler {

    /**
     * 处理 Exception 异常
     *
     * @param e                  异常
     * @return
     */
    @ExceptionHandler(value = ExecutionException.class)
    public void exceptionHandler(Exception e) {
       log.error("操作缓存时遇到异常{}", e.getMessage());
    }
}
