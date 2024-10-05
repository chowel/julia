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
 * 游戏得分
 * </p>
 *
 * @author chowel
 * @since 2024-10-03
 */
@Getter
@Setter
@TableName("game_score")
public class GameScoreEntity extends BaseEntity {

    /**
     * 主键id
     */
    @TableId(value = "score_id", type = IdType.AUTO)
    private Long scoreId;

    /**
     * Game Id
     */
    @TableField("game_id")
    private Long gameId;

    /**
     * 游戏编号
     */
    @TableField("game_no")
    private String gameNo;

    /**
     * 玩家1 Id
     */
    @TableField("player_i_id")
    private Integer playerIId;

    /**
     * 玩家1得分
     */
    @TableField("player_i_score")
    private Integer playerIScore;

    /**
     * 玩家1最后牌面
     */
    @TableField("player_i_poker")
    private String playerIPoker;

    /**
     * 玩家2 Id
     */
    @TableField("player_ii_id")
    private Integer playerIiId;

    /**
     * 玩家2得分
     */
    @TableField("player_ii_score")
    private Integer playerIiScore;

    /**
     * 玩家2最后牌面
     */
    @TableField("player_ii_poker")
    private String playerIiPoker;

    /**
     * 玩家3 Id
     */
    @TableField("player_iii_id")
    private Integer playerIiiId;

    /**
     * 玩家3得分
     */
    @TableField("player_iii_score")
    private Integer playerIiiScore;

    /**
     * 玩家3最后牌面
     */
    @TableField("player_iii_poker")
    private String playerIiiPoker;

    /**
     * 玩家4 Id
     */
    @TableField("player_iv_id")
    private Integer playerIvId;

    /**
     * 玩家4得分
     */
    @TableField("player_iv_score")
    private Integer playerIvScore;

    /**
     * 玩家4最后牌面
     */
    @TableField("player_iv_poker")
    private String playerIvPoker;
}
