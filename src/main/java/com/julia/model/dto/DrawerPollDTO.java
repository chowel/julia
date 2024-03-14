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

    @ApiModelProperty("付款人账号")
    private String drawer;

    @ApiModelProperty("平台订单号")
    private String fortuneNo;
}
