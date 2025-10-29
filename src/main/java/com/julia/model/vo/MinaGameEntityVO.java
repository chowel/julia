package com.julia.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.julia.tool.BaseEntity;

import org.springframework.beans.BeanUtils;
import com.julia.entity.MinaGameEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * Game
 * </p>
 *
 * @author chowel
 * @since 2025-10-27
 */
@Getter
@Setter
@ApiModel(value = "MinaGameEntityVO", description = "GameVO")
public class MinaGameEntityVO extends BaseEntity {

    @ApiModelProperty("id")
    private Long gameId;

    @ApiModelProperty("玩家id")
    private Long playerId;

    @ApiModelProperty("游戏编码")
    private String gameNo;

    @ApiModelProperty("游戏类型 1,firday  2,saturday")
    private Integer gameType;

    @ApiModelProperty("本金")
    private Integer principal;

    @ApiModelProperty("获利")
    private Integer profit;

    @ApiModelProperty("状态  1 创建 2 结算")
    private Integer status;

    @ApiModelProperty("内容")
    private String content;

    @Override
    public String toString() {
        return "{" +
            "gameId = " + gameId +
            ", playerId = " + playerId +
            ", gameNo = " + gameNo +
            ", gameType = " + gameType +
            ", principal = " + principal +
            ", profit = " + profit +
            ", status = " + status +
            ", content = " + content +
        "}";
    }
}
