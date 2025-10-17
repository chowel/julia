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
 * 系统菜单
 * </p>
 *
 * @author chowel
 * @since 2025-10-17
 */
@Getter
@Setter
@TableName("mina_sys_menu")
public class MinaSysMenuEntity extends BaseEntity {

    /**
     * 主键id
     */
    @TableId(value = "pact_id", type = IdType.AUTO)
    private Integer pactId;

    /**
     * 名称
     */
    @TableField("pact_name")
    private String pactName;

    /**
     * 父id
     */
    @TableField("parent_id")
    private Integer parentId;

    /**
     * 1:菜单大类 2:菜单項 3:接口
     */
    @TableField("pact_type")
    private Integer pactType;

    /**
     * 接口关键字 3有
     */
    @TableField("perms")
    private String perms;

    /**
     * 菜单路由 2有
     */
    @TableField("url")
    private String url;

    /**
     * 菜单图标2有
     */
    @TableField("icon")
    private String icon;

    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 是否删除 1 正常 0 删除
     */
    @TableField("che_del")
    private Integer cheDel;
}
