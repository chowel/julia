package com.julia.model.vo;

import com.julia.tool.BaseEntity;
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
 * @since 2024-02-29
 */
@Getter
@Setter
@ApiModel(value = "FortuneEntityVO", description = "财神VO")
public class FortuneApiVO extends BaseEntity {

    @ApiModelProperty("平台交易号")
    private String fortuneNo;

    @ApiModelProperty("外部交易号")
    private String transactNo;

    @ApiModelProperty("订单号")
    private String orderId;

    @ApiModelProperty("金额")
    private Integer amount;

    private Integer cId;

    @ApiModelProperty("姓")
    private String firstName;

    @ApiModelProperty("名")
    private String lastName;

    @ApiModelProperty("0 : 未操作 1: 收款单已发送  2:失败 3:过期未操作 4:成功")
    private Integer status;

    @ApiModelProperty("p用户回调 0:未通知  1:收到成功 2:收到失败")
    private Integer checkCallback;

    @ApiModelProperty("操作时间 时间戳")
    private Long doneTime;


    @ApiModelProperty("失败原因")
    private String msg;

    @ApiModelProperty("付款人账号")
    private String drawer;

    @Override
    public String toString() {
        return "{" +
            ", fortuneNo = " + fortuneNo +
            ", transactNo = " + transactNo +
            ", orderId = " + orderId +
            ", amount = " + amount +
            ", cId = " + cId +
            ", firstName = " + firstName +
            ", lastName = " + lastName +
            ", status = " + status +
            ", checkCallback = " + checkCallback +
            ", doneTime = " + doneTime +
            ", msg = " + msg +
            ", drawer = " + drawer +
        "}";
    }
}
