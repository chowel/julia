package com.julia.model;

import com.julia.tool.Poker;
import com.julia.tool.PokerMoldForFive;

import java.util.List;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2024-10-04 20:17
 **/
public class PlayerGameRo {

    public PlayerGameRo(){}

    private List<Poker> prePokers;

    private List<PokerMoldForFive> molds;

    private Integer playerId;

    private Integer gameType;

    private String gameNo;

    private String roomIde;

    public List<Poker> getPrePokers() {
        return prePokers;
    }

    public void setPrePokers(List<Poker> prePokers) {
        this.prePokers = prePokers;
    }

    public List<PokerMoldForFive> getMolds() {
        return molds;
    }

    public void setMolds(List<PokerMoldForFive> molds) {
        this.molds = molds;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public Integer getGameType() {
        return gameType;
    }

    public void setGameType(Integer gameType) {
        this.gameType = gameType;
    }

    public String getGameNo() {
        return gameNo;
    }

    public void setGameNo(String gameNo) {
        this.gameNo = gameNo;
    }

    public String getRoomIde() {
        return roomIde;
    }

    public void setRoomIde(String roomIde) {
        this.roomIde = roomIde;
    }
}
