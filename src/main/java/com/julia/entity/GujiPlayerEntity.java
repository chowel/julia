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
 * @since 2025-12-17
 */
@Getter
@Setter
@TableName("guji_player")
public class GujiPlayerEntity extends BaseEntity {

    /**
     * 用户ID
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    /**
     * 微信unionId
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
    @TableField("gold")
    private Integer gold;

    /**
     * 能量
     */
    @TableField("energy")
    private Integer energy;

    /**
     * 最大能量
     */
    @TableField("max_energy")
    private Integer maxEnergy;

    /**
     * 钻石
     */
    @TableField("diamond")
    private Integer diamond;

    /**
     * 关卡
     */
    @TableField("battle_level")
    private Integer battleLevel;

    /**
     * 敌人波数
     */
    @TableField("battle_wave")
    private Integer battleWave;

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

    /**
     * 是否新手 1 新手
     */
    @TableField("guide_child")
    private Integer guideChild;

    /**
     * 游戏速度
     */
    @TableField("play_speed")
    private Integer playSpeed;

    /**
     * 箱子等级
     */
    @TableField("box_level")
    private Integer boxLevel;

    /**
     * 开箱子经验
     */
    @TableField("box_exp")
    private Integer boxExp;

    /**
     * 箱子点数
     */
    @TableField("box_point")
    private Integer boxPoint;

    /**
     * 左宝箱数量
     */
    @TableField("box_left")
    private Integer boxLeft;

    /**
     * 中间宝箱
     */
    @TableField("box_mid")
    private Integer boxMid;

    /**
     * 右边宝箱
     */
    @TableField("box_right")
    private Integer boxRight;

    /**
     * 代理数
     */
    @TableField("proxy_count")
    private Integer proxyCount;

    /**
     * 是否代理 0 不是 1 是
     */
    @TableField("proxy")
    private Integer proxy;
}
