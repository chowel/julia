package com.julia.model.alipay;

import lombok.Getter;
import lombok.Setter;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2025-03-17 15:55
 **/

@Getter
@Setter
public class AlipayGoodDetails {

    private String  goodsId;

    private String goodsName;

    private Integer quantity;

    private String price;

    private String alipayGoodsId;

    private String goodsCategory;

    private String body;

    private String showUrl;
}
