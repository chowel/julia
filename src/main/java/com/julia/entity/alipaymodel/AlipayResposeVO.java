package com.julia.entity.alipaymodel;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2025-03-31 22:25
 **/
@Getter
@Setter
public class AlipayResposeVO {

    private String tradeStatus;

    private String totalAmount;

    private String orderNo;

    private String outOrderNo;

    private LocalDateTime paytime;


}
