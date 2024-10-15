package com.julia.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.julia.tool.BaseEntity;

import com.julia.tool.Poker;
import org.springframework.beans.BeanUtils;
import com.julia.entity.GameThirteenEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
@ApiModel(value = "GameThirteenEntityVO", description = "13游戏详情VO")
public class GameThirteenEntityVO extends BaseEntity {

    @ApiModelProperty("主键")
    private Long detailId;

    @ApiModelProperty("gameId")
    private Long gameId;

    @ApiModelProperty("gameNo")
    private String gameNo;

    @ApiModelProperty("玩家id")
    private Long playerId;

    @ApiModelProperty("头部")
    private String headgear;

    @ApiModelProperty("扑克id ,分割")
    private String headgearPokers;

    @ApiModelProperty("头部得分")
    private Integer headScore;

    @ApiModelProperty("中部")
    private String midgear;

    @ApiModelProperty("扑克id ,分割")
    private String midgearPoker;

    @ApiModelProperty("中部得分")
    private Integer midScore;

    @ApiModelProperty("底部")
    private String basegear;

    @ApiModelProperty("扑克id ,分割")
    private String basegearPoker;

    @ApiModelProperty("底部得分")
    private Integer baseScore;

    @ApiModelProperty("总得分")
    private Integer total;
    @ApiModelProperty("头牌")
    private List<Poker> headPokers;
    @ApiModelProperty("中牌")
    private List<Poker> midPokers;
    @ApiModelProperty("底牌")
    private List<Poker> basePokers;

    @Override
    public String toString() {
        return "GameThirteenEntityVO{" +
                "detailId=" + detailId +
                ", gameId=" + gameId +
                ", gameNo='" + gameNo + '\'' +
                ", playerId=" + playerId +
                ", headgear='" + headgear + '\'' +
                ", headgearPokers='" + headgearPokers + '\'' +
                ", headScore=" + headScore +
                ", midgear='" + midgear + '\'' +
                ", midgearPoker='" + midgearPoker + '\'' +
                ", midScore=" + midScore +
                ", basegear='" + basegear + '\'' +
                ", basegearPoker='" + basegearPoker + '\'' +
                ", baseScore=" + baseScore +
                ", total=" + total +
                ", headPokers=" + headPokers +
                ", midPokers=" + midPokers +
                ", basePokers=" + basePokers +
                '}';
    }
}
