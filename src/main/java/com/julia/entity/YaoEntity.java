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
 * 账号
 * </p>
 *
 * @author chowel
 * @since 2023-11-01
 */
@Getter
@Setter
@TableName("yao")
public class YaoEntity extends BaseEntity {

    /**
     * 用户表主键id
     */
    @TableId(value = "yao_id", type = IdType.AUTO)
    private Integer yaoId;

    /**
     * 登录名
     */
    @TableField("login_name")
    private String loginName;

    /**
     * 用户名/昵称
     */
    @TableField("user_name")
    private String userName;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 电话号码
     */
    @TableField("phone")
    private String phone;

    /**
     * 头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 1:正常   2:删除/停用
     */
    @TableField("che_del")
    private Integer cheDel;

    /**
     * 菜单 power_id
     */
    @TableField("role_id")
    private Integer roleId;

    /**
     * 接口权限 power_id
     */
    @TableField("auth_id")
    private Integer authId;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;
}
