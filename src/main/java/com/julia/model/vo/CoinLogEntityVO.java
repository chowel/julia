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
 * 记录
 * </p>
 *
 * @author chowel
 * @since 2025-09-28
 */
@Getter
@Setter
@ApiModel(value = "CoinLogEntityVO", description = "记录VO")
public class CoinLogEntityVO extends BaseEntity {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("访问人数")
    private Integer coin;

    @ApiModelProperty("时间 年月日")
    private String coinDate;

    @ApiModelProperty("访问地址")
    private String coinUrl;

    @Override
    public String toString() {
        return "{" +
            "id = " + id +
            ", coin = " + coin +
            ", coinDate = " + coinDate +
            ", coinUrl = " + coinUrl +
        "}";
    }
}
