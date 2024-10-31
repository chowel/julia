package com.julia.model;

import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2024-10-05 01:51
 **/
public class AliveGameRo {

    public AliveGameRo() {

    }

    private Integer playId;

    private Integer gameType;

    private String roomIde;

    private String gameIde;

    /**
     * 最小入场金额
     */
    private Integer least;

    /**
     * 每局金额
     */
    private Integer agame;

    public Integer getPlayId() {
        return playId;
    }

    public void setPlayId(Integer playId) {
        this.playId = playId;
    }

    public Integer getGameType() {
        return gameType;
    }

    public void setGameType(Integer gameType) {
        this.gameType = gameType;
    }

    public String getRoomIde() {
        return roomIde;
    }

    public void setRoomIde(String roomIde) {
        this.roomIde = roomIde;
    }

    public String getGameIde() {
        return gameIde;
    }

    public void setGameIde(String gameIde) {
        this.gameIde = gameIde;
    }

    public Integer getLeast() {
        return least;
    }

    public void setLeast(Integer least) {
        this.least = least;
    }

    public Integer getAgame() {
        return agame;
    }

    public void setAgame(Integer agame) {
        this.agame = agame;
    }
}
