package com.julia.model.HuiYuan;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2025-09-18 09:46
 **/
@Data
public class BillDetailsResponse {

    /**
     * 返回码
     */
    private Integer retCode;

    /**
     * 返回消息
     */
    private String retMsg;

    /**
     * 代理商 ID
     */
    private String agentId;

    /**
     * 订单号
     */
    private String billNo;

    /**
     * 产品编码
     */
    private String productCode;

    /**
     * 产品名称（含中文）
     */
    private String productName;

    /**
     * 面值价格
     */
    private BigDecimal parPrice;

    /**
     * 实付金额（采购价）
     */
    private BigDecimal purchaseAmt;

    /**
     * 订单状态
     * 0: 成功, 其他: 失败/处理中（根据接口文档）
     */
    private Integer billStatus;

    /**
     * 签名
     */
    private String sign;

    @Override
    public String toString() {
        return "HuiYuanResponse{" +
                "retCode=" + retCode +
                ", retMsg='" + retMsg + '\'' +
                ", agentId='" + agentId + '\'' +
                ", billNo='" + billNo + '\'' +
                ", productCode='" + productCode + '\'' +
                ", productName='" + productName + '\'' +
                ", parPrice=" + parPrice +
                ", purchaseAmt=" + purchaseAmt +
                ", billStatus=" + billStatus +
                ", sign='" + sign + '\'' +
                '}';
    }
}
