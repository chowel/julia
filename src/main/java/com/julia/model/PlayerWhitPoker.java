package com.julia.model;

import com.julia.tool.Poker;

import java.util.List;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2024-10-04 01:34
 **/
public class PlayerWhitPoker {
    private Long playId;

    private String nickName;

    private String loginName;

    private String roomIde;

    private String gameIde;

    private List<Poker> prePokers;

    private List<Poker> handPokers;

    private Integer score;

    public Long getPlayId() {
        return playId;
    }

    public void setPlayId(Long playId) {
        this.playId = playId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
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

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
