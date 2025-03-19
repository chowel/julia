package com.julia.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2024-03-14 14:05
 **/

@Getter
@Setter
@ApiModel(value = "DrawerPollDTO", description = "DrawerPollDTO")
public class DrawerPollDTO {

    @ApiModelProperty("支付网址")
    private String payUrl;

    @ApiModelProperty("订单编号")
    private String orderNo;
}
