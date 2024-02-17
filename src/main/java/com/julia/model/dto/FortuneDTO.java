package com.julia.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2024-02-15 22:56
 **/
@Getter
@Setter
@ApiModel(value = "FortuneDTO", description = "财神")
public class FortuneDTO {

    @ApiModelProperty("订单号")
    private String orderId;

    @ApiModelProperty("金额")
    private Integer amount;
}
