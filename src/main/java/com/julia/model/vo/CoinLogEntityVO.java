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
 * 车队加进记录
 * </p>
 *
 * @author chowel
 * @since 2023-11-23
 */
@Getter
@Setter
@ApiModel(value = "CoinLogEntityVO", description = "车队加进记录VO")
public class CoinLogEntityVO extends BaseEntity {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("操作账号id")
    private Integer yId;

    @ApiModelProperty("车队id")
    private Integer cId;

    @ApiModelProperty("金额")
    private Integer coin;

    @Override
    public String toString() {
        return "{" +
            "id = " + id +
            ", yId = " + yId +
            ", cId = " + cId +
            ", coin = " + coin +
        "}";
    }
}
