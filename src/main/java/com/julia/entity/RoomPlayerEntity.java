package com.julia.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.julia.tool.BaseEntity;
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
@TableName("room_player")
public class RoomPlayerEntity extends BaseEntity {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 房间id
     */
    @TableField("room_id")
    private Integer roomId;

    /**
     * 1:13水
     */
    @TableField("game_type")
    private Integer gameType;

    /**
     * 0:普通玩家 1:房间创建用户
     */
    @TableField("init")
    private Integer init;

    /**
     * 玩家id
     */
    @TableField("player_id")
    private Long playerId;

    /**
     * 玩家昵称
     */
    @TableField("nick_name")
    private String nickName;

    /**
     * 1:在线  0:退出
     */
    @TableField("online")
    private Integer online;

    /**
     * 房间ide
     */
    @TableField("room_flag")
    private String roomFlag;
}
