package com.julia.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2023-11-08 11:43
 **/

@Getter
@Setter
@ApiModel(value = "InputRocketDTO", description = "输入")
public class InputRocketDTO {

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

}
