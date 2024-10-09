package com.julia.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.julia.tool.BaseEntity;

import org.springframework.beans.BeanUtils;
import com.julia.entity.GameScoreEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "GameScoreEntityVO", description = "游戏得分VO")
public class GameScoreEntityVO extends BaseEntity {

    @ApiModelProperty("主键id")
    private Long scoreId;

    @ApiModelProperty("Game Id")
    private Long gameId;

    @ApiModelProperty("游戏编号")
    private String gameNo;

    @ApiModelProperty("玩家1 Id")
    private Integer playerIId;

    @ApiModelProperty("玩家1得分")
    private String playerIScore;

    @ApiModelProperty("玩家1最后牌面")
    private String playerIPoker;

    @ApiModelProperty("玩家2 Id")
    private Integer playerIiId;

    @ApiModelProperty("玩家2得分")
    private String playerIiScore;

    @ApiModelProperty("玩家2最后牌面")
    private String playerIiPoker;

    @ApiModelProperty("玩家3 Id")
    private Integer playerIiiId;

    @ApiModelProperty("玩家3得分")
    private String playerIiiScore;

    @ApiModelProperty("玩家3最后牌面")
    private String playerIiiPoker;

    @ApiModelProperty("玩家4 Id")
    private Integer playerIvId;

    @ApiModelProperty("玩家4得分")
    private String playerIvScore;

    @ApiModelProperty("玩家4最后牌面")
    private String playerIvPoker;

    @Override
    public String toString() {
        return "{" +
            "scoreId = " + scoreId +
            ", gameId = " + gameId +
            ", gameNo = " + gameNo +
            ", playerIId = " + playerIId +
            ", playerIScore = " + playerIScore +
            ", playerIPoker = " + playerIPoker +
            ", playerIiId = " + playerIiId +
            ", playerIiScore = " + playerIiScore +
            ", playerIiPoker = " + playerIiPoker +
            ", playerIiiId = " + playerIiiId +
            ", playerIiiScore = " + playerIiiScore +
            ", playerIiiPoker = " + playerIiiPoker +
            ", playerIvId = " + playerIvId +
            ", playerIvScore = " + playerIvScore +
            ", playerIvPoker = " + playerIvPoker +
        "}";
    }
}
