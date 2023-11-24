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
    ;


    private final String key;

}
