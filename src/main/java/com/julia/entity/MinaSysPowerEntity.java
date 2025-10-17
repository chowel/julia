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
 * 系统角色
 * </p>
 *
 * @author chowel
 * @since 2025-10-17
 */
@Getter
@Setter
@TableName("mina_sys_power")
public class MinaSysPowerEntity extends BaseEntity {

    /**
     * 主键id
     */
    @TableId(value = "power_id", type = IdType.AUTO)
    private Integer powerId;

    /**
     * 权限名称
     */
    @TableField("power_name")
    private String powerName;

    /**
     * 权限关键字
     */
    @TableField("power_key")
    private String powerKey;

    /**
     * 1:菜单 2:接口
     */
    @TableField("power_type")
    private Integer powerType;

    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 1:正常 0:删除
     */
    @TableField("che_del")
    private Integer cheDel;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;
}
