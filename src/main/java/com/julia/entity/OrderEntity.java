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
 * 
 * </p>
 *
 * @author chowel
 * @since 2025-03-19
 */
@Getter
@Setter
@TableName("order")
public class OrderEntity extends BaseEntity {

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
     * 1:创建订单 2:成功 3:失败 4:用户取消 5:超时失败
     */
    @TableField("status")
    private Integer status;
}
