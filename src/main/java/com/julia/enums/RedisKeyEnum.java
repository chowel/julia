package com.julia.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName RedisKeyEnums
 * @Description Redis Key枚举
 * @Version 1.0
 **/
@AllArgsConstructor
@Getter
public enum RedisKeyEnum {


    /**
     * redis保活
     */
    ALIVE("YOKI:ALIVE"),
    /**
     * web客户
     */
    CLIENTSUBMIT("YOKI:CLIENTSUBMIT:"),
    /**
     * 日访问 set
     */
    VISITDAILY("YOKI:VISITDAILY"),
    /**
     * 日访问并填表 set
     */
    VISITDAILYHASINPUT("YOKI:VISITDAILY:HASINPUT"),
    /**
     * 无车队公共池
     */
    HOSTNAMELIST("YOKI:HOSTNAMELIST"),
    /**
     * 域名访问
     */
    HOSTNAMEVISIT("YOKI:HOSTNAMEVISIT:"),
    /**
     * 域名访问并输入
     */
    HOSTNAMEVISITINPUT("YOKI:HOSTNAMEVISIT:INPUT:"),
    /**
     * 车队池
     */
    CAR_POND("JULIA:CAR_POND:"),
    /**
     * 成功进入收银台
     */
    DEPOSIT("JULIA:DEPOSIT_POND:"),
    /**
     * 轮询游标
     */
    POLLING("JULIA:POLLING"),
    ;


    private final String key;

}
