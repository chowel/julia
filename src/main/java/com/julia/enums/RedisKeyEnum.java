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
     * 车队
     */
    CAR_ALIVE("JULIA:CAR_ALIVE_ZSET"),
    /**
     * 有车队公共池
     */
    COMMON_POND("JULIA:COMMON_POND:"),
    /**
     * 无车队公共池
     */
    COMMON_POOL("JULIA:COMMON_POOL:"),
    /**
     * 车队池
     */
    CAR_POND("JULIA:CAR_POND:"),
    ;


    private final String key;

}
