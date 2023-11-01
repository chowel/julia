package com.julia.tool;

/**
 * @program: skychain
 * @description:
 * @author: Chowel.Master
 * @create: 2022-09-24 22:39
 **/
public class Rv<T> {

    private static final long serialVersionUID = -1L;

    private String msg;
    private T t;

    public Rv(String msg, T t){
        this.msg = msg;
        this.t = t;
    }

    public Rv(T t){
        this.msg = "OK";
        this.t = t;
    }

    public static Rv OK(){
        return new Rv("OK",null);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }
}
