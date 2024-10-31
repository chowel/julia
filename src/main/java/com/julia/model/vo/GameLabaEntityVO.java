package com.julia.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.julia.tool.BaseEntity;

import org.springframework.beans.BeanUtils;
import com.julia.entity.GameLabaEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * laba游戏详情
 * </p>
 *
 * @author chowel
 * @since 2024-10-31
 */
@Getter
@Setter
@ApiModel(value = "GameLabaEntityVO", description = "laba游戏详情VO")
public class GameLabaEntityVO extends BaseEntity {

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("gameId")
    private Long gameId;

    @ApiModelProperty("gameNo")
    private String gameNo;

    @ApiModelProperty("玩家id")
    private Long playerId;

    @ApiModelProperty("每局注码")
    private Integer agame;

    @ApiModelProperty("得分")
    private Integer score;

    @ApiModelProperty("结果")
    private String res;

    @Override
    public String toString() {
        return "{" +
            "id = " + id +
            ", gameId = " + gameId +
            ", gameNo = " + gameNo +
            ", playerId = " + playerId +
            ", agame = " + agame +
            ", score = " + score +
            ", res = " + res +
        "}";
    }
}
