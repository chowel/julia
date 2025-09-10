package com.julia.entity.alipaymodel;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * @program: julia
 * @description: 创建订单
 * @author: Chowel.Master
 * @create: 2025-03-31 13:49
 **/
@Getter
@Setter
public class PayByAliPay {

    private Long price;

    private String payKey;

    private String subject;

    private String orderNo;

    private String noticeUrl;

    private String outOrderNo;

}
