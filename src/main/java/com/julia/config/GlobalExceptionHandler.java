package com.julia.config;

import cn.dev33.satoken.exception.DisableLoginException;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import com.julia.tool.JuliaException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @program: julia
 * @description: 全局异常处理
 * @author: Chowel.Master
 * @create: 2023-10-30 15:07
 **/

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * @Description: Exception 500 异常
     * @Param:
     * @return:
     * @Author: Chowel.Master
     * @Date:
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> errorHandler(Exception ex, HttpServletRequest httpServletRequest) {
        ex.printStackTrace();
        log.error("requestUrl: " + httpServletRequest.getRequestURL());
        log.error("Exception: {}", ex.getMessage());
        return new ResponseEntity<String>(
                "{\"message\":\"" + "inside error" + "\"}", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * @Description: 自定义 异常 510
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    @ExceptionHandler(value = JuliaException.class)
    public ResponseEntity<String> flowsExcHandler(JuliaException ex, HttpServletRequest httpServletRequest) {
//        ex.printStackTrace();
        log.error("requestUrl: " + httpServletRequest.getRequestURL());
        log.error("Exception: {}", ex.getMsg());
        return new ResponseEntity<String>(
                "{\"message\":\"" + ex.getMsg() + "\"}", HttpStatus.NOT_EXTENDED);
    }

    @ExceptionHandler(value = NotLoginException.class)
    public ResponseEntity<String> notLoginExcHandler(NotLoginException ex, HttpServletRequest httpServletRequest) {
        ex.printStackTrace();
        log.error("requestUrl: " + httpServletRequest.getRequestURL());
        log.error("Exception: {}", ex.getMessage());
        return new ResponseEntity<>(
                "{\"message\":\"" + ex.getMessage() + "\"}", HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
    }

    @ExceptionHandler(value = NotPermissionException.class)
    public ResponseEntity<String> notPermissionExcHandler(NotPermissionException ex, HttpServletRequest httpServletRequest) {
        ex.printStackTrace();
        log.error("requestUrl: " + httpServletRequest.getRequestURL());
        log.error("Exception: {}", ex.getMessage());
        return new ResponseEntity<>(
                "{\"message\":\"" + ex.getMessage() + "\"}", HttpStatus.LOOP_DETECTED);
    }

    @ExceptionHandler(value = DisableLoginException.class)
    public ResponseEntity<String> disableLoginExcHandler(DisableLoginException ex, HttpServletRequest httpServletRequest) {
        ex.printStackTrace();
        log.error("requestUrl: " + httpServletRequest.getRequestURL());
        log.error("Exception: {}", ex.getMessage());
        return new ResponseEntity<>(
                "{\"message\":\"" + ex.getMessage() + "\"}", HttpStatus.BANDWIDTH_LIMIT_EXCEEDED);
    }

    /**
     * 参数校验
     * @param ex
     * @param httpServletRequest
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<String> methodArgumentNotValidHandler(MethodArgumentNotValidException ex,  HttpServletRequest httpServletRequest) {
        ex.printStackTrace();
        log.error("requestUrl: " + httpServletRequest.getRequestURL());
        log.error("Exception: {}", ex.getMessage());
        return new ResponseEntity<String>(
                "{\"message\":\"" + Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage() + "\"}", HttpStatus.NOT_EXTENDED);
    }
}
