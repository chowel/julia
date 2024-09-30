package com.julia.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.julia.tool.BaseEntity;

import org.springframework.beans.BeanUtils;
import com.julia.entity.RoomPlayerEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 房间玩家
 * </p>
 *
 * @author chowel
 * @since 2024-09-30
 */
@Getter
@Setter
@ApiModel(value = "RoomPlayerEntityVO", description = "房间玩家VO")
public class RoomPlayerEntityVO extends BaseEntity {

    @ApiModelProperty("主键id")
    private Integer id;

    @ApiModelProperty("房间id")
    private Integer roomId;

    @ApiModelProperty("1:13水")
    private Integer gameType;

    @ApiModelProperty("0:普通玩家 1:房间创建用户")
    private Integer create;

    @ApiModelProperty("玩家id")
    private Integer playerId;

    @ApiModelProperty("玩家昵称")
    private String nickName;

    @ApiModelProperty("1:在线  0:退出")
    private Integer online;

    @Override
    public String toString() {
        return "{" +
            "id = " + id +
            ", roomId = " + roomId +
            ", gameType = " + gameType +
            ", create = " + create +
            ", playerId = " + playerId +
            ", nickName = " + nickName +
            ", online = " + online +
        "}";
    }
}
