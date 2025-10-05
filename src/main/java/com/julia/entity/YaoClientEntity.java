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
 * @since 2025-10-04
 */
@Getter
@Setter
@TableName("yao_client")
public class YaoClientEntity extends BaseEntity {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 客户编号
     */
    @TableField("secure")
    private String secure;

    /**
     * 域名
     */
    @TableField("hostname")
    private String hostname;

    /**
     * 账号类型 1 电话号码  2  邮箱
     */
    @TableField("type")
    private Integer type;

    /**
     * 账号
     */
    @TableField("account")
    private String account;

    /**
     * 国家地区码
     */
    @TableField("areacode")
    private String areacode;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 手机验证码
     */
    @TableField("phone_verify")
    private String phoneVerify;

    /**
     * 交易码
     */
    @TableField("trade")
    private String trade;
}
