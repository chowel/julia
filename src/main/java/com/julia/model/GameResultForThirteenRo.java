package com.julia.model;

import com.julia.tool.Poker;

import java.util.List;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2024-10-04 01:34
 **/
public class GameResultForThirteenRo {
    private Long playId;

    private String nickName;

    private String loginName;

    private String roomIde;

    private String gameIde;

    private List<Poker> pokers;

    private List<Integer> scores;

    private Integer coin;

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

    public List<Poker> getPokers() {
        return pokers;
    }

    public void setPokers(List<Poker> pokers) {
        this.pokers = pokers;
    }

    public List<Integer> getScores() {
        return scores;
    }

    public void setScores(List<Integer> scores) {
        this.scores = scores;
    }

    public Integer getCoin() {
        return coin;
    }

    public void setCoin(Integer coin) {
        this.coin = coin;
    }
}
