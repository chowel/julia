package com.julia.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.julia.tool.BaseEntity;

import org.springframework.beans.BeanUtils;
import com.julia.entity.GameRoomEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 游戏房间
 * </p>
 *
 * @author chowel
 * @since 2024-09-30
 */
@Getter
@Setter
@ApiModel(value = "GameRoomEntityVO", description = "游戏房间VO")
public class GameRoomEntityVO extends BaseEntity {

    @ApiModelProperty("主键Id")
    private Integer roomId;

    @ApiModelProperty("1:13水")
    private Integer gameType;

    @ApiModelProperty("0:创建 1:游戏中 3:结束")
    private Integer status;

    @ApiModelProperty("房间人数")
    private Integer players;

    @ApiModelProperty("游戏局数")
    private Integer play;

    @ApiModelProperty("每局抽水")
    private Integer servr;

    @ApiModelProperty("总抽水")
    private Integer total;

    @ApiModelProperty("房间标识")
    private String flag;

    @ApiModelProperty("创建房间用户id")
    private Integer playerId;

    @Override
    public String toString() {
        return "{" +
            "roomId = " + roomId +
            ", gameType = " + gameType +
            ", status = " + status +
            ", players = " + players +
            ", play = " + play +
            ", servr = " + servr +
            ", total = " + total +
            ", flag = " + flag +
        "}";
    }
}
