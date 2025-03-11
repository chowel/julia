package com.julia.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.julia.tool.BaseEntity;

import org.springframework.beans.BeanUtils;
import com.julia.entity.StackPlayerEntity;
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
 * @since 2025-03-09
 */
@Getter
@Setter
@ApiModel(value = "StackPlayerEntityVO", description = "玩家VO")
public class StackPlayerEntityVO extends BaseEntity {

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台帐号下会返回")
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

    @ApiModelProperty("用户金币")
    private Integer coin;

    @ApiModelProperty("1 租客 2 房东")
    private Integer houseType;

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
            ", coin = " + coin +
            ", houseType = " + houseType +
            ", status = " + status +
            ", wechat = " + wechat +
            ", qq = " + qq +
            ", email = " + email +
            ", note = " + note +
        "}";
    }
}
