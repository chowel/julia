package com.julia.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2023-11-07 19:04
 **/
@Getter
@Setter
@ApiModel(value = "CarOperaDTO", description = "CarOperaDTO操作提交")
public class CarOperaDTO {
    @ApiModelProperty("主键id")
    private Long rocketId;

    @ApiModelProperty("实际收款")
    private Integer realPay;

    @ApiModelProperty("c用户id")
    private Integer cId;
}
