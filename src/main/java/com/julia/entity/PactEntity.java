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
 * 菜单/接口权限項
 * </p>
 *
 * @author chowel
 * @since 2023-11-01
 */
@Getter
@Setter
@TableName("pact")
public class PactEntity extends BaseEntity {

    /**
     * 主键id
     */
    @TableId(value = "pact_id", type = IdType.AUTO)
    private Integer pactId;

    /**
     * 菜单/接口名称
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
     * 接口关键字 3才有
     */
    @TableField("perms")
    private String perms;

    /**
     * 菜单关键字 2才有
     */
    @TableField("url")
    private String url;

    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 图标 2才有
     */
    @TableField("icon")
    private String icon;

    /**
     * 0:删除 1:正常
     */
    @TableField("che_del")
    private Integer cheDel;
}
