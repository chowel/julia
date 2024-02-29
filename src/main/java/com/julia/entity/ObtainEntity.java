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
 * 车队下账号
 * </p>
 *
 * @author chowel
 * @since 2024-02-29
 */
@Getter
@Setter
@TableName("obtain")
public class ObtainEntity extends BaseEntity {

    /**
     * 主键id
     */
    @TableId(value = "obtain_id", type = IdType.AUTO)
    private Integer obtainId;

    /**
     * 关联车队id
     */
    @TableField("yao_id")
    private Integer yaoId;

    /**
     * 账号
     */
    @TableField("name")
    private String name;

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
     * 状态 0 停用 1启用
     */
    @TableField("status")
    private Integer status;

    /**
     * 每日使用次数
     */
    @TableField("cout")
    private Integer cout;

    /**
     * 备注
     */
    @TableField("note")
    private String note;
}
