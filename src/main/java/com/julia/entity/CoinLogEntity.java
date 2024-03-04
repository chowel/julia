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
 * 上货记录
 * </p>
 *
 * @author chowel
 * @since 2024-03-04
 */
@Getter
@Setter
@TableName("coin_log")
public class CoinLogEntity extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 操作者id
     */
    @TableField("y_id")
    private Integer yId;

    /**
     * 上押id
     */
    @TableField("c_id")
    private Integer cId;

    /**
     * 盘方id
     */
    @TableField("p_id")
    private Integer pId;

    /**
     * 1 收单 2 上米
     */
    @TableField("type")
    private Integer type;

    /**
     * coin
     */
    @TableField("coin")
    private Integer coin;

    /**
     * 订单号
     */
    @TableField("fortune_no")
    private String fortuneNo;
}
