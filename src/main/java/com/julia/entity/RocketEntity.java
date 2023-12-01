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
 * 火箭业务
 * </p>
 *
 * @author chowel
 * @since 2023-11-13
 */
@Getter
@Setter
@TableName("rocket")
public class RocketEntity extends BaseEntity {

    /**
     * 主键id
     */
    @TableId(value = "rocket_id", type = IdType.AUTO)
    private Long rocketId;

    /**
     * 图片地址
     */
    @TableField("url")
    private String url;

    /**
     * 金额
     */
    @TableField("amount")
    private Integer amount;

    /**
     * 实际收款
     */
    @TableField("real_pay")
    private Integer realPay;

    /**
     * 订单号
     */
    @TableField("order_id")
    private String orderId;

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
     * c用户id
     */
    @TableField("c_id")
    private Integer cId;

    /**
     * p用户id
     */
    @TableField("p_id")
    private Integer pId;

    /**
     * 状态 0 : 未操作 1: 已操作成功  2:已操作失败 3:过期未操作
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
     * 失败原因
     */
    @TableField("msg")
    private String msg;
}
