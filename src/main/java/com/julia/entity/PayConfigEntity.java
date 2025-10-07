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
 * @since 2025-03-15
 */
@Getter
@Setter
@TableName("pay_config")
public class PayConfigEntity extends BaseEntity {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 标题
     */
    @TableField("title")
    private String title;

    /**
     * 编码
     */
    @TableField("code")
    private String code;

    /**
     * 赠送金币
     */
    @TableField("gold")
    private Integer gold;

    /**
     * 赠送钻石
     */
    @TableField("ore")
    private Integer ore;

    /**
     * 价格 分
     */
    @TableField("price")
    private Long price;

    /**
     * 状态
     */
    @TableField("status")
    private Integer status;
}
