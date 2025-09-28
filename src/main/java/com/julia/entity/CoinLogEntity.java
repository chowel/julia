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
 * 记录
 * </p>
 *
 * @author chowel
 * @since 2025-09-28
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
     * 访问人数
     */
    @TableField("coin")
    private Integer coin;

    /**
     * 时间 年月日
     */
    @TableField("coin_date")
    private String coinDate;

    /**
     * 访问地址
     */
    @TableField("coin_url")
    private String coinUrl;
}
