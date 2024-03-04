package com.julia.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.julia.tool.BaseEntity;

import org.springframework.beans.BeanUtils;
import com.julia.entity.CoinLogEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 上货记录
 * </p>
 *
 * @author chowel
 * @since 2024-03-04
 */
@Getter
@Setter
@ApiModel(value = "CoinLogEntityVO", description = "上货记录VO")
public class CoinLogEntityVO extends BaseEntity {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("操作者id")
    private Integer yId;

    @ApiModelProperty("上押id")
    private Integer cId;

    @ApiModelProperty("盘方id")
    private Integer pId;

    @ApiModelProperty("1 收单 2 上米")
    private Integer type;

    @ApiModelProperty("coin")
    private Integer coin;

    @ApiModelProperty("订单号")
    private String fortuneNo;

    @Override
    public String toString() {
        return "{" +
            "id = " + id +
            ", yId = " + yId +
            ", cId = " + cId +
            ", pId = " + pId +
            ", type = " + type +
            ", coin = " + coin +
            ", fortuneNo = " + fortuneNo +
        "}";
    }
}
