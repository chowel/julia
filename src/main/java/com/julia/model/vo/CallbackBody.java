package com.julia.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @program: marina
 * @description: 回调对象
 * @author: Chowel.Master
 * @create: 2025-04-22 13:33
 **/

@Getter
@Setter
@ApiModel(value = "CallbackBody", description = "回调对象")
public class CallbackBody {

    private String sign;

    private String orderNo;

    private String outOrderNo;

    private String totalAmount;

    private String tradeStatus;

    private String payTime;


}
