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
 * 游戏房间
 * </p>
 *
 * @author chowel
 * @since 2024-10-18
 */
@Getter
@Setter
@TableName("game_room")
public class GameRoomEntity extends BaseEntity {

    /**
     * 主键Id
     */
    @TableId(value = "room_id", type = IdType.AUTO)
    private Integer roomId;

    /**
     * 1:13水
     */
    @TableField("game_type")
    private Integer gameType;

    /**
     * 0:创建 1:游戏中 2:结束
     */
    @TableField("status")
    private Integer status;

    /**
     * 房间人数
     */
    @TableField("players")
    private Integer players;

    /**
     * 游戏局数
     */
    @TableField("play")
    private Integer play;

    /**
     * 每局抽水
     */
    @TableField("servr")
    private Integer servr;

    /**
     * 总抽水
     */
    @TableField("total")
    private Integer total;

    /**
     * 房间标识
     */
    @TableField("flag")
    private String flag;

    /**
     * 最小入场金额
     */
    @TableField("least")
    private Integer least;

    /**
     * 每局金额
     */
    @TableField("agame")
    private Integer agame;
}
