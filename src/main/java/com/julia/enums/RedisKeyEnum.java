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
    ROOMPLAYERS("JULIA:FIT:ROOMPLAYERS:"),

    ;


    private final String key;

}
