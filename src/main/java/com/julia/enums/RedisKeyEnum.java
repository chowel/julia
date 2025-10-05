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
     * 有车队公共池
     */
    VISITDAILY("YOKI:VISITDAILY:"),
    /**
     * 无车队公共池
     */
    COMMON_POOL("JULIA:COMMON_POOL:"),
    /**
     * 财神公共池
     */
    FORTUNE_POOL("JULIA:FORTUNE_POOL:"),
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
