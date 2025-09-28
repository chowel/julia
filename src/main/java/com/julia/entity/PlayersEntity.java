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
 * @since 2025-09-28
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
     * 用户标识
     */
    @TableField("secure")
    private String secure;

    /**
     * web_URL
     */
    @TableField("web_url")
    private String webUrl;

    /**
     * 访问IP
     */
    @TableField("web_ip")
    private String webIp;

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
     * 数据验证 0 没通过 1 通过
     */
    @TableField("verify")
    private Integer verify;
}
