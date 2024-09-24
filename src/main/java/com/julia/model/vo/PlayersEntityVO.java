package com.julia.model.vo;


import com.julia.tool.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "PlayersEntityVO", description = "用户表VO")
public class PlayersEntityVO extends BaseEntity {

    @ApiModelProperty("用户ID")
    private Long playId;

    @ApiModelProperty("微信唯一标识符")
    private String unionId;

    @ApiModelProperty("支付宝唯一标识")
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

    @ApiModelProperty("0:普通用户 ")
    private Integer playerType;

    @ApiModelProperty("1 正常 0 停用(删除)")
    private Integer status;

    @Override
    public String toString() {
        return "{" +
            "playId = " + playId +
            ", unionId = " + unionId +
            ", openId = " + openId +
            ", nickName = " + nickName +
            ", avatarUrl = " + avatarUrl +
            ", phoneNumber = " + phoneNumber +
            ", loginName = " + loginName +
            ", password = " + password +
            ", coin = " + coin +
            ", playerType = " + playerType +
            ", status = " + status +
        "}";
    }
}
