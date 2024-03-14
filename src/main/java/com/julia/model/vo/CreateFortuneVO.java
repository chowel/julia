package com.julia.model.vo;

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
@ApiModel(value = "CreateFortuneVO", description = "创建财神返回VO")
public class CreateFortuneVO {

    @ApiModelProperty("收银台地址")
    private String payUrl;

    @ApiModelProperty("商户订单号/备注")
    private String orderId;

    @ApiModelProperty("金额")
    private Integer amount;

    @ApiModelProperty("平台交易号")
    private String fortuneNo;
}
