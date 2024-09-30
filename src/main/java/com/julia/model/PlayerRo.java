package com.julia.model;

import java.util.Objects;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2024-09-30 21:43
 **/
public class PlayerRo {

    private Long playId;

    private String nickName;

    private String loginName;

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

    public Integer getCoin() {
        return coin;
    }

    public void setCoin(Integer coin) {
        this.coin = coin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PlayerRo playerRo = (PlayerRo) o;
        return Objects.equals(playId, playerRo.playId) &&
                Objects.equals(nickName, playerRo.nickName) &&
                Objects.equals(loginName, playerRo.loginName) &&
                Objects.equals(coin, playerRo.coin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playId, nickName, loginName, coin);
    }
}
