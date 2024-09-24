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
 * 用户表
 * </p>
 *
 * @author chowel
 * @since 2024-09-24
 */
@Getter
@Setter
@TableName("players")
public class PlayersEntity extends BaseEntity {

    /**
     * 用户ID
     */
    @TableId(value = "play_id", type = IdType.AUTO)
    private Long playId;

    /**
     * 微信唯一标识符
     */
    @TableField("union_id")
    private String unionId;

    /**
     * 支付宝唯一标识
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
     * 用户金币
     */
    @TableField("coin")
    private Integer coin;

    /**
     * 0:普通用户 
     */
    @TableField("player_type")
    private Integer playerType;

    /**
     * 1 正常 0 停用(删除)
     */
    @TableField("status")
    private Integer status;
}
