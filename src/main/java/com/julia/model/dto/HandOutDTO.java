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
@ApiModel(value = "HandOutDTO", description = "车队分发收款单")
public class HandOutDTO {

    @ApiModelProperty("财神id")
    private Long fortuneId;

    @ApiModelProperty("收单账号")
    private String pid;

    @ApiModelProperty("平台订单号")
    private String fortuneNo;
}
