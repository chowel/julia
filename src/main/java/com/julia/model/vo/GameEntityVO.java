package com.julia.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.julia.tool.BaseEntity;

import org.springframework.beans.BeanUtils;
import com.julia.entity.GameEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 游戏
 * </p>
 *
 * @author chowel
 * @since 2024-10-03
 */
@Getter
@Setter
@ApiModel(value = "GameEntityVO", description = "游戏VO")
public class GameEntityVO extends BaseEntity {

    @ApiModelProperty("主键id")
    private Long gameId;

    @ApiModelProperty("游戏编号")
    private String gameNo;

    @ApiModelProperty("房间id")
    private Integer roomId;

    @ApiModelProperty("房间Flag")
    private String roomFlag;

    @ApiModelProperty("1:13水")
    private Integer gameType;

    @ApiModelProperty("1:游戏中 0:结束")
    private Integer status;

    @ApiModelProperty("牌面JSON")
    private String pokers;

    @Override
    public String toString() {
        return "{" +
            "gameId = " + gameId +
            ", gameNo = " + gameNo +
            ", roomId = " + roomId +
            ", roomFlag = " + roomFlag +
            ", gameType = " + gameType +
            ", status = " + status +
            ", pokers = " + pokers +
        "}";
    }
}
