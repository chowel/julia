package com.julia.model;

import com.julia.tool.Poker;

import java.util.List;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2024-10-04 20:17
 **/
public class PlayerGameRo {

    private List<Poker> prePokers;

    private List<Poker> handPokers;

    private Integer playerId;

    private Integer gameType;

    private String gameNo;

    private String roomIde;

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

    public List<Poker> getPrePokers() {
        return prePokers;
    }

    public void setPrePokers(List<Poker> prePokers) {
        this.prePokers = prePokers;
    }

    public List<Poker> getHandPokers() {
        return handPokers;
    }

    public void setHandPokers(List<Poker> handPokers) {
        this.handPokers = handPokers;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public String getGameNo() {
        return gameNo;
    }

    public void setGameNo(String gameNo) {
        this.gameNo = gameNo;
    }
}
