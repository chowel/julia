package com.julia.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.julia.tool.BaseEntity;

import org.springframework.beans.BeanUtils;
import com.julia.entity.OrderEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author chowel
 * @since 2025-03-19
 */
@Getter
@Setter
@ApiModel(value = "OrderEntityVO", description = "VO")
public class OrderEntityVO extends BaseEntity {

    @ApiModelProperty("主键id")
    private Integer orderId;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("金额")
    private Long total;

    @ApiModelProperty("主题")
    private String subject;

    @ApiModelProperty("销售产品码")
    private String productCode;

    @ApiModelProperty("玩家id")
    private Integer playerId;

    @ApiModelProperty("1:创建订单 2:成功 3:失败 4:用户取消 5:超时失败")
    private Integer status;

    @Override
    public String toString() {
        return "{" +
            "orderId = " + orderId +
            ", orderNo = " + orderNo +
            ", total = " + total +
            ", subject = " + subject +
            ", productCode = " + productCode +
            ", playerId = " + playerId +
            ", status = " + status +
        "}";
    }
}
