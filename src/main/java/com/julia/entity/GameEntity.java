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
 * 游戏
 * </p>
 *
 * @author chowel
 * @since 2024-10-31
 */
@Getter
@Setter
@TableName("game")
public class GameEntity extends BaseEntity {

    /**
     * 主键id
     */
    @TableId(value = "game_id", type = IdType.AUTO)
    private Long gameId;

    /**
     * 游戏编号
     */
    @TableField("game_no")
    private String gameNo;

    /**
     * 房间id
     */
    @TableField("room_id")
    private Integer roomId;

    /**
     * 房间Flag
     */
    @TableField("room_flag")
    private String roomFlag;

    /**
     * 1:13水
     */
    @TableField("game_type")
    private Integer gameType;

    /**
     * 1:游戏中 0:结束
     */
    @TableField("status")
    private Integer status;

    /**
     * 牌面JSON
     */
    @TableField("pokers")
    private String pokers;

    /**
     * 拉霸结果
     */
    @TableField("laba_res")
    private String labaRes;
}
