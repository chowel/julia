package com.julia.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.julia.tool.BaseEntity;

import org.springframework.beans.BeanUtils;
import com.julia.entity.FortuneEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 财神
 * </p>
 *
 * @author chowel
 * @since 2024-02-18
 */
@Getter
@Setter
@ApiModel(value = "FortuneEntityVO", description = "财神VO")
public class FortuneEntityVO extends BaseEntity {

    @ApiModelProperty("主键id")
    private Long fortuneId;

    @ApiModelProperty("订单号")
    private String orderId;

    @ApiModelProperty("金额")
    private Integer amount;

    @ApiModelProperty("收款账号")
    private String payId;

    @ApiModelProperty("盘方id")
    private Integer pId;

    @ApiModelProperty("车队id")
    private Integer cId;

    @ApiModelProperty("姓")
    private String firstName;

    @ApiModelProperty("名")
    private String lastName;

    @ApiModelProperty("0 : 未操作 1: 收款单已发送  2:收款成功 3:收款失败")
    private Integer status;

    @ApiModelProperty("p用户回调 0:未通知  1:收到成功 2:收到失败")
    private Integer checkCallback;

    @ApiModelProperty("操作时间 时间戳")
    private Long doneTime;

    @ApiModelProperty("车队发送收款时间")
    private Long handoutTime;

    @ApiModelProperty("失败原因")
    private String msg;

    @ApiModelProperty("付款人账号")
    private String drawer;

    @Override
    public String toString() {
        return "{" +
            "fortuneId = " + fortuneId +
            ", orderId = " + orderId +
            ", amount = " + amount +
            ", payId = " + payId +
            ", pId = " + pId +
            ", cId = " + cId +
            ", firstName = " + firstName +
            ", lastName = " + lastName +
            ", status = " + status +
            ", checkCallback = " + checkCallback +
            ", doneTime = " + doneTime +
            ", handoutTime = " + handoutTime +
            ", msg = " + msg +
            ", drawer = " + drawer +
        "}";
    }
}
