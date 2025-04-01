package com.julia.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.julia.tool.BaseEntity;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 游戏充值订单表
 * </p>
 *
 * @author chowel
 * @since 2025-04-01
 */
@Getter
@Setter
@TableName("game_order")
public class GameOrderEntity extends BaseEntity {

    /**
     * 主键id
     */
    @TableId(value = "order_id", type = IdType.AUTO)
    private Integer orderId;

    /**
     * 订单编号
     */
    @TableField("order_no")
    private String orderNo;

    /**
     * 金额
     */
    @TableField("total")
    private Long total;

    /**
     * 主题
     */
    @TableField("subject")
    private String subject;

    /**
     * 销售产品码
     */
    @TableField("product_code")
    private String productCode;

    /**
     * 玩家id
     */
    @TableField("player_id")
    private Integer playerId;

    /**
     * 1:创建订单 2:成功 3:失败 4:用户取消 5:超时失败 6:预创建
     */
    @TableField("status")
    private Integer status;

    /**
     * 支付宝交易凭证号
     */
    @TableField("trade_no")
    private String tradeNo;

    /**
     * 买家支付宝用户号
     */
    @TableField("buyer_id")
    private String buyerId;

    /**
     * 买家支付宝账号
     */
    @TableField("buyer_logon_id")
    private String buyerLogonId;

    /**
     * WAIT_BUYER_PAY	等待买家付款。
TRADE_CLOSED 未付款交易超时关闭，或支付完成后全额退款。
TRADE_SUCCESS	交易支付成功。
TRADE_FINISHED	交易结束，不可退款。
     */
    @TableField("trade_status")
    private String tradeStatus;

    /**
     * 本次交易支付的订单金额
     */
    @TableField("total_amount")
    private Long totalAmount;

    /**
     * 商家在收益中实际收到的款项
     */
    @TableField("receipt_amount")
    private Long receiptAmount;

    /**
     * 可开发票的金额
     */
    @TableField("invoice_amount")
    private Long invoiceAmount;

    /**
     * 该笔交易创建的时间
     */
    @TableField("gmt_create")
    private LocalDateTime gmtCreate;

    /**
     * 付款时间
     */
    @TableField("gmt_payment")
    private LocalDateTime gmtPayment;

    /**
     * 退款时间
     */
    @TableField("gmt_refund")
    private LocalDateTime gmtRefund;

    /**
     * 交易关闭时间
     */
    @TableField("gmt_close")
    private LocalDateTime gmtClose;

    /**
     * 玩家昵称
     */
    @TableField("player_name")
    private String playerName;
}
