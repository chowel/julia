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
     * 玩家进行中的游戏
     */
    PLAYERCACHE("JULIA:FIT:PLAYERCACHE:"),
    /**
     * 房间玩家
     */
    ROOMPLAYERS("JULIA:FIT:ROOMPLAYERS:"),
    /**
     * 房间缓存
     */
    ROOMCACHE("JULIA:FIT:ROOMCACHE:"),
    /**
     * 待发的牌
     */
    NOTSENDPOKER("JULIA:FIT:NOTSENDPOKER:"),
    /**
     * 发给玩家的牌
     */
    PLAYERPOKERS("JULIA:FIT:PLAYERPOKERS:"),
    /**
     * 牌局提交牌的玩家数
     */
    RECEIVES("JULIA:FIT:RECEIVES:"),
    /**
     * 玩家进行中的游戏
     */
    ALIVEGAME("JULIA:FIT:ALIVEGAME:"),
    ;


    private final String key;

}
