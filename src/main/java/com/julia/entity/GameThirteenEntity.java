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
 * 13游戏详情
 * </p>
 *
 * @author chowel
 * @since 2024-10-10
 */
@Getter
@Setter
@TableName("game_thirteen")
public class GameThirteenEntity extends BaseEntity {

    /**
     * 主键
     */
    @TableId(value = "detail_id", type = IdType.AUTO)
    private Long detailId;

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
     * 头部
     */
    @TableField("headgear")
    private String headgear;

    /**
     * 扑克id ,分割
     */
    @TableField("headgear_pokers")
    private String headgearPokers;

    /**
     * 头部得分
     */
    @TableField("head_score")
    private Integer headScore;

    /**
     * 中部
     */
    @TableField("midgear")
    private String midgear;

    /**
     * 扑克id ,分割
     */
    @TableField("midgear_poker")
    private String midgearPoker;

    /**
     * 中部得分
     */
    @TableField("mid_score")
    private Integer midScore;

    /**
     * 底部
     */
    @TableField("basegear")
    private String basegear;

    /**
     * 扑克id ,分割
     */
    @TableField("basegear_poker")
    private String basegearPoker;

    /**
     * 底部得分
     */
    @TableField("base_score")
    private Integer baseScore;

    /**
     * 总得分
     */
    @TableField("total")
    private Integer total;
}
