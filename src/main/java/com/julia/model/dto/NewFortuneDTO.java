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
@ApiModel(value = "NewFortuneDTO", description = "创建财神Dto")
public class NewFortuneDTO {

    @ApiModelProperty("商户订单号/备注")
    private String orderId;

    @ApiModelProperty("金额")
    private Integer amount;

    @ApiModelProperty("回调地址")
    private String noticeUrl;
}
