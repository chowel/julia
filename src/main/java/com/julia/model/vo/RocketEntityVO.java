package com.julia.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.julia.tool.BaseEntity;

import org.springframework.beans.BeanUtils;
import com.julia.entity.RocketEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 火箭业务
 * </p>
 *
 * @author chowel
 * @since 2023-11-01
 */
@Getter
@Setter
@ApiModel(value = "RocketEntityVO", description = "火箭业务VO")
public class RocketEntityVO extends BaseEntity {

    @ApiModelProperty("主键id")
    private Long rocketId;

    @ApiModelProperty("图片地址")
    private String url;

    @ApiModelProperty("金额")
    private Integer amount;

    @ApiModelProperty("订单号")
    private String orderId;

    @ApiModelProperty("姓")
    private String firstName;

    @ApiModelProperty("名")
    private String lastName;

    @ApiModelProperty("操作人id")
    private Integer yaoId;

    @ApiModelProperty("状态 0 : 未操作 1: 已操作")
    private Integer status;

    @ApiModelProperty("操作时间 时间戳")
    private Long doneTime;

    @Override
    public String toString() {
        return "{" +
            "rocketId = " + rocketId +
            ", url = " + url +
            ", amount = " + amount +
            ", orderId = " + orderId +
            ", firstName = " + firstName +
            ", lastName = " + lastName +
            ", yaoId = " + yaoId +
            ", status = " + status +
            ", doneTime = " + doneTime +
        "}";
    }
}
