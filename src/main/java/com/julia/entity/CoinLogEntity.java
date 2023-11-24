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
 * 车队加进记录
 * </p>
 *
 * @author chowel
 * @since 2023-11-23
 */
@Getter
@Setter
@TableName("coin_log")
public class CoinLogEntity extends BaseEntity {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 操作账号id
     */
    @TableField("y_id")
    private Integer yId;

    /**
     * 车队id
     */
    @TableField("c_id")
    private Integer cId;

    /**
     * 金额
     */
    @TableField("coin")
    private Integer coin;
}
