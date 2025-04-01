package com.julia.entity.alipaymodel;

import lombok.Getter;
import lombok.Setter;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2025-03-31 22:25
 **/
@Getter
@Setter
public class AlipayResposeVO {

    private String tradeStats;

    private String totalAmount;

    private String orderNo;
}
