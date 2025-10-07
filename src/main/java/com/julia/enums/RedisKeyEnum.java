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
    CAR_ALIVE("TOWER:ALIVE"),
    /**
     * 有车队公共池
     */
    COMMON_POND("JULIA:COMMON_POND:"),
    /**
     * 无车队公共池
     */
    COMMON_POOL("JULIA:COMMON_POOL:"),
    /**
     * 财神公共池
     */
    FORTUNE_POOL("JULIA:FORTUNE_POOL:"),
    /**
     * 列表下载时间
     */
    DOWNTIME("JULIA:GAME:DOWNTIME"),
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
    /**
     * 待回调订单 TOWER:WAITORDER: + OrderNo
     */
    WAITORDER("TOWER:WAITORDER:"),
    /**
     * jiahe
     */
    JIAHE("TOWER:JIAHE"),
    /**
     * 玩家排序
     */
    PLAYERSZET("TOWER:PLAYERS")
    ;


    private final String key;

}
