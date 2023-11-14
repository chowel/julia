package com.julia.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.julia.tool.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * <p>
 * 权限表
 * </p>
 *
 * @author chowel
 * @since 2023-11-01
 */
@Getter
@Setter
@ApiModel(value = "CarOrderVO", description = "车队收单")
public class CarOrderVO {
    @ApiModelProperty("主键id")
    private Long rocketId;

    @ApiModelProperty("图片地址")
    private String url;

    @ApiModelProperty("金额")
    private Integer amount;

    @ApiModelProperty("实际收款")
    private Integer realPay;

    @ApiModelProperty("订单号")
    private String orderId;

    @ApiModelProperty("姓")
    private String firstName;

    @ApiModelProperty("名")
    private String lastName;

    @ApiModelProperty("状态 0 : 未操作 1: 已操作成功  2:已操作失败")
    private Integer status;

    @ApiModelProperty("p用户回调 0:未通知  1:收到成功 2:收到失败")
    private Integer checkCallback;

    @ApiModelProperty("操作时间 时间戳")
    private Long doneTime;

    @ApiModelProperty("操作时间 时间戳")
    private String msg;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;

}
