package com.julia.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.julia.tool.BaseEntity;

import org.springframework.beans.BeanUtils;
import com.julia.entity.GujiPlayerEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "GujiPlayerEntityVO", description = "玩家VO")
public class GujiPlayerEntityVO extends BaseEntity {

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("微信unionId")
    private String unionId;

    @ApiModelProperty("用户唯一标识")
    private String openId;

    @ApiModelProperty("用户名")
    private String nickName;

    @ApiModelProperty("头像URL")
    private String avatarUrl;

    @ApiModelProperty("电话")
    private String phoneNumber;

    @ApiModelProperty("登录名")
    private String loginName;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("金币")
    private Integer gold;

    @ApiModelProperty("能量")
    private Integer energy;

    @ApiModelProperty("最大能量")
    private Integer maxEnergy;

    @ApiModelProperty("钻石")
    private Integer diamond;

    @ApiModelProperty("关卡")
    private Integer battleLevel;

    @ApiModelProperty("敌人波数")
    private Integer battleWave;

    @ApiModelProperty("1 正常 0 停用(删除)")
    private Integer status;

    @ApiModelProperty("微信号")
    private String wechat;

    @ApiModelProperty("qq")
    private String qq;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("预约留言")
    private String note;

    @ApiModelProperty("token")
    private String token;

    @ApiModelProperty("是否新手 1 新手")
    private Integer guideChild;

    @ApiModelProperty("游戏速度")
    private Integer playSpeed;

    @ApiModelProperty("箱子等级")
    private Integer boxLevel;

    @ApiModelProperty("开箱子经验")
    private Integer boxExp;

    @ApiModelProperty("箱子点数")
    private Integer boxPoint;

    @ApiModelProperty("左宝箱数量")
    private Integer boxLeft;

    @ApiModelProperty("中间宝箱")
    private Integer boxMid;

    @ApiModelProperty("右边宝箱")
    private Integer boxRight;

    @ApiModelProperty("代理数")
    private Integer proxyCount;

    @ApiModelProperty("是否代理 0 不是 1 是")
    private Integer proxy;

    @Override
    public String toString() {
        return "{" +
            "userId = " + userId +
            ", unionId = " + unionId +
            ", openId = " + openId +
            ", nickName = " + nickName +
            ", avatarUrl = " + avatarUrl +
            ", phoneNumber = " + phoneNumber +
            ", loginName = " + loginName +
            ", password = " + password +
            ", gold = " + gold +
            ", energy = " + energy +
            ", maxEnergy = " + maxEnergy +
            ", diamond = " + diamond +
            ", battleLevel = " + battleLevel +
            ", battleWave = " + battleWave +
            ", status = " + status +
            ", wechat = " + wechat +
            ", qq = " + qq +
            ", email = " + email +
            ", note = " + note +
            ", guideChild = " + guideChild +
            ", playSpeed = " + playSpeed +
            ", boxLevel = " + boxLevel +
            ", boxExp = " + boxExp +
            ", boxPoint = " + boxPoint +
            ", boxLeft = " + boxLeft +
            ", boxMid = " + boxMid +
            ", boxRight = " + boxRight +
            ", proxyCount = " + proxyCount +
            ", proxy = " + proxy +
        "}";
    }
}
