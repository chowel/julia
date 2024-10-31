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
 * laba游戏详情
 * </p>
 *
 * @author chowel
 * @since 2024-10-31
 */
@Getter
@Setter
@TableName("game_laba")
public class GameLabaEntity extends BaseEntity {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * gameId
     */
    @TableField("game_id")
    private Long gameId;

    /**
     * gameNo
     */
    @TableField("game_no")
    private String gameNo;

    /**
     * 玩家id
     */
    @TableField("player_id")
    private Long playerId;

    /**
     * 每局注码
     */
    @TableField("agame")
    private Integer agame;

    /**
     * 得分
     */
    @TableField("score")
    private Integer score;

    /**
     * 结果
     */
    @TableField("res")
    private String res;
}
