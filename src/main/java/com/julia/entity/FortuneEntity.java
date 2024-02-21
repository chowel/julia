package com.julia.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.julia.tool.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 财神
 * </p>
 *
 * @author chowel
 * @since 2024-02-18
 */
@Getter
@Setter
@TableName("fortune")
public class FortuneEntity extends BaseEntity {

    /**
     * 主键id
     */
    @TableId(value = "fortune_id", type = IdType.AUTO)
    private Long fortuneId;

    /**
     * 订单号
     */
    @TableField("order_id")
    private String orderId;

    /**
     * 金额
     */
    @TableField("amount")
    private Integer amount;

    /**
     * 收款账号
     */
    @TableField("pay_id")
    private String payId;

    /**
     * 盘方id
     */
    @TableField("p_id")
    private Integer pId;

    /**
     * 车队id
     */
    @TableField("c_id")
    private Integer cId;

    /**
     * 姓
     */
    @TableField("first_name")
    private String firstName;

    /**
     * 名
     */
    @TableField("last_name")
    private String lastName;

    /**
     * 0 : 未操作 1: 收款单已发送  2:已操作失败 3:过期未操作
     */
    @TableField("status")
    private Integer status;

    /**
     * p用户回调 0:未通知  1:收到成功 2:收到失败
     */
    @TableField("check_callback")
    private Integer checkCallback;

    /**
     * 操作时间 时间戳
     */
    @TableField("done_time")
    private Long doneTime;

    /**
     * 车队发送收款时间
     */
    @TableField("handout_time")
    private Long handoutTime;

    /**
     * 失败原因
     */
    @TableField("msg")
    private String msg;

    /**
     * 付款人账号
     */
    @TableField("drawer")
    private String drawer;
}
