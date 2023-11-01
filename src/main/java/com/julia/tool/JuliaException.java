package com.julia.tool;

/**
 * @program: julia
 * @description: 项目定义异常
 * @author: Chowel.Master
 * @create: 2023-10-30 15:10
 **/
public class JuliaException extends RuntimeException{

    public JuliaException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public JuliaException(String errorMsg) {
        this.msg = errorMsg;
    }
    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
