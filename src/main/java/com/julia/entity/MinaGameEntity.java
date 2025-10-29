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
 * Game
 * </p>
 *
 * @author chowel
 * @since 2025-10-27
 */
@Getter
@Setter
@TableName("mina_game")
public class MinaGameEntity extends BaseEntity {

    /**
     * id
     */
    @TableId(value = "game_id", type = IdType.AUTO)
    private Long gameId;

    /**
     * 玩家id
     */
    @TableField("player_id")
    private Long playerId;

    /**
     * 游戏编码
     */
    @TableField("game_no")
    private String gameNo;

    /**
     * 游戏类型 1,firday  2,saturday
     */
    @TableField("game_type")
    private Integer gameType;

    /**
     * 本金
     */
    @TableField("principal")
    private Integer principal;

    /**
     * 获利
     */
    @TableField("profit")
    private Integer profit;

    /**
     * 状态  1 创建 2 结算
     */
    @TableField("status")
    private Integer status;

    /**
     * 内容
     */
    @TableField("content")
    private String content;
}
