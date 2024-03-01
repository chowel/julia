package com.julia.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2024-03-01 17:11
 **/
@Getter
@Setter
public class FortuneRedis {
    /**
     * 付款人账号
     */
    private String drawer;

    /**
     * 盘方id
     */
    private Integer pId;

    /**
     * 平台交易号
     */
    private String fortuneNo;

    /**
     * 金额
     */
    private Integer amount;

    /**
     * 订单号
     */
    private String orderId;
}
