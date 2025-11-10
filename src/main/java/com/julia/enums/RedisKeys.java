package com.julia.enums;

/**
 * @program: marina
 * @description:
 * @author: Chowel.Master
 * @create: 2025-03-27 14:52
 **/
public enum RedisKeys {
    /**
     * redis保活
     */
    ALIVE("MINA:ALIVE"),
    /**
     * 玩家池
     */
    PLAYERINFO("MINA:PLAYER:%s"),
    /**
     * 玩家金币
     */
    PLAYERCOIN("MINA:PLAYERCOIN:%s"),
    /**
     * 玩家StaturdyGame
     */
    STATURDAYGAMEPOOL("MINA:STAGAME:%s"),
    /**
     * 玩家fridayGame
     */
    FRIDAYGAMEPOOL("MINA:FRIGAME:%s"),
    ;

    private final String keyTemplate;

    RedisKeys(String keyTemplate){
        this.keyTemplate = keyTemplate;
    }

    public String getKey(String... args){
        return String.format(keyTemplate,(Object[]) args);
    }
}
