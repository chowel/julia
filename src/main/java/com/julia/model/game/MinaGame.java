package com.julia.model.game;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Arrays;
import java.util.Objects;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2025-10-27 17:55
 **/
@Data
public class MinaGame {

    /**
     * 玩家id
     */
    private Long playerId;

    /**
     * 游戏编码
     */
    private String gameNo;

    /**
     * 游戏类型 1,firday  2,saturday
     */
    private Integer gameType;

    /**
     * 本金
     */
    private Integer principal;

    /**
     * 获利
     */
    private Integer profit;

    /**
     * 状态  1 创建 2 结算
     */
    private Integer status;

    /**
     * 内容
     */
    private String[] contents;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MinaGame minaGame = (MinaGame) o;
        return Objects.equals(playerId, minaGame.playerId) &&
                Objects.equals(gameNo, minaGame.gameNo) &&
                Objects.equals(gameType, minaGame.gameType) &&
                Objects.equals(principal, minaGame.principal) &&
                Objects.equals(profit, minaGame.profit) &&
                Objects.equals(status, minaGame.status) &&
                Arrays.equals(contents, minaGame.contents);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(playerId, gameNo, gameType, principal, profit, status);
        result = 31 * result + Arrays.hashCode(contents);
        return result;
    }
}
