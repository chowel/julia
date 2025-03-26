package com.julia.model.alipay;

import com.alipay.api.domain.GoodsDetail;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @program: julia
 * @description: 支付宝
 * @author: Chowel.Master
 * @create: 2025-03-17 15:42
 **/


@Getter
@Setter
public class AliPayCreate {
    /**
     * @Description: 必填
     */
    private String outTradeNo;
    /**
     * @Description: 必填
     */
    private String totalAmount;
    /**
     * @Description: 必填
     */
    private String subject;
    /**
     * @Description: 必填 QUICK_WAP_WAY
     */
    private String productCode;
    /**
     * @Description: 选填
     */
    private String authToken;
    /**
     * @Description: 选填 用户付款中途退出返回商户网站的地址
     */
    private String quitUrl;
    /**
     * @Description: 选填 绝对超时时间
     */
    private String timeExpire;
    /**
     * @Description: 选填 商户传入业务信息，具体值要和支付宝约定，应用于安全，营销等参数直传场景，格式为json格式
     */
    private String businessParams;

    private String passbackParams;

    private String MerchantOrderNo;

    private List<GoodsDetail> goods;


}
