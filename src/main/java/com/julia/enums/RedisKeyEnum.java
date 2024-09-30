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
     * 房间玩家
     */
    ROOMPLAYERS("JULIA:FIT:ROOMPLAYERS:"),
    /**
     * 房间最大玩家数
     */
    ROOMMAXPLAYERS("JULIA:FIT:ROOMMAX:"),
    ;


    private final String key;

}
