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
 * 玩家
 * </p>
 *
 * @author chowel
 * @since 2025-09-16
 */
@Getter
@Setter
@TableName("stack_player")
public class StackPlayerEntity extends BaseEntity {

    /**
     * 用户ID
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    /**
     * 用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台帐号下会返回
     */
    @TableField("union_id")
    private String unionId;

    /**
     * 用户唯一标识
     */
    @TableField("open_id")
    private String openId;

    /**
     * 用户名
     */
    @TableField("nick_name")
    private String nickName;

    /**
     * 头像URL
     */
    @TableField("avatar_url")
    private String avatarUrl;

    /**
     * 电话
     */
    @TableField("phone_number")
    private String phoneNumber;

    /**
     * 登录名
     */
    @TableField("login_name")
    private String loginName;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 金币
     */
    @TableField("coin")
    private Integer coin;

    /**
     * 关卡
     */
    @TableField("pass")
    private Integer pass;

    /**
     * 钻石
     */
    @TableField("mason")
    private Integer mason;

    /**
     * 1 正常 0 停用(删除)
     */
    @TableField("status")
    private Integer status;

    /**
     * 微信号
     */
    @TableField("wechat")
    private String wechat;

    /**
     * qq
     */
    @TableField("qq")
    private String qq;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 预约留言
     */
    @TableField("note")
    private String note;
}
