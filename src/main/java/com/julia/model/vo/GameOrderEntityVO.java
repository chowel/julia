package com.julia.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.julia.tool.BaseEntity;
import java.time.LocalDateTime;

import org.springframework.beans.BeanUtils;
import com.julia.entity.GameOrderEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 游戏充值订单表
 * </p>
 *
 * @author chowel
 * @since 2025-04-03
 */
@Getter
@Setter
@ApiModel(value = "GameOrderEntityVO", description = "游戏充值订单表VO")
public class GameOrderEntityVO extends BaseEntity {

    @ApiModelProperty("主键id")
    private Integer orderId;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("金额")
    private Long total;

    @ApiModelProperty("主题")
    private String subject;

    @ApiModelProperty("销售产品码")
    private String productCode;

    @ApiModelProperty("玩家id")
    private Integer playerId;

    @ApiModelProperty("1:创建订单 2:成功 3:失败 4:用户取消 5:超时失败")
    private Integer status;

    @ApiModelProperty("支付宝交易凭证号")
    private String tradeNo;

    @ApiModelProperty("买家支付宝用户号")
    private String buyerId;

    @ApiModelProperty("买家支付宝账号")
    private String buyerLogonId;

    @ApiModelProperty("WAIT_BUYER_PAY	等待买家付款。 TRADE_CLOSED 未付款交易超时关闭，或支付完成后全额退款。 TRADE_SUCCESS	交易支付成功。 TRADE_FINISHED	交易结束，不可退款。")
    private String tradeStatus;

    @ApiModelProperty("本次交易支付的订单金额")
    private Long totalAmount;

    @ApiModelProperty("商家在收益中实际收到的款项")
    private Long receiptAmount;

    @ApiModelProperty("可开发票的金额")
    private Long invoiceAmount;

    @ApiModelProperty("该笔交易创建的时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("付款时间")
    private LocalDateTime gmtPayment;

    @ApiModelProperty("退款时间")
    private LocalDateTime gmtRefund;

    @ApiModelProperty("交易关闭时间")
    private LocalDateTime gmtClose;

    @ApiModelProperty("玩家昵称")
    private String playerName;

    @ApiModelProperty("回调地址")
    private String url;

    @ApiModelProperty("外部订单号")
    private String outOrderNo;

    @ApiModelProperty("外部商户编号")
    private String merchantNo;

    @Override
    public String toString() {
        return "{" +
            "orderId = " + orderId +
            ", orderNo = " + orderNo +
            ", total = " + total +
            ", subject = " + subject +
            ", productCode = " + productCode +
            ", playerId = " + playerId +
            ", status = " + status +
            ", tradeNo = " + tradeNo +
            ", buyerId = " + buyerId +
            ", buyerLogonId = " + buyerLogonId +
            ", tradeStatus = " + tradeStatus +
            ", totalAmount = " + totalAmount +
            ", receiptAmount = " + receiptAmount +
            ", invoiceAmount = " + invoiceAmount +
            ", gmtCreate = " + gmtCreate +
            ", gmtPayment = " + gmtPayment +
            ", gmtRefund = " + gmtRefund +
            ", gmtClose = " + gmtClose +
            ", playerName = " + playerName +
            ", url = " + url +
            ", outOrderNo = " + outOrderNo +
            ", merchantNo = " + merchantNo +
        "}";
    }
}
