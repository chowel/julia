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
    ALIVE("MINA:ALIVE"),
    /**
     * redis保活
     */
    GAMEPOOL("MINA:GAMEPOOL:"),

    ;


    private final String key;

}
