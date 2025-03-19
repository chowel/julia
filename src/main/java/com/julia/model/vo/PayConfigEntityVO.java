package com.julia.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.julia.tool.BaseEntity;

import org.springframework.beans.BeanUtils;
import com.julia.entity.PayConfigEntity;
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
 * @since 2025-03-15
 */
@Getter
@Setter
@ApiModel(value = "PayConfigEntityVO", description = "VO")
public class PayConfigEntityVO extends BaseEntity {

    @ApiModelProperty("主键id")
    private Integer id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("赠送金币")
    private Integer gold;

    @ApiModelProperty("赠送钻石")
    private Integer ore;

    @ApiModelProperty("价格 分")
    private Long price;

    @ApiModelProperty("状态")
    private Integer status;

    @Override
    public String toString() {
        return "{" +
            "id = " + id +
            ", title = " + title +
            ", gold = " + gold +
            ", ore = " + ore +
            ", price = " + price +
            ", status = " + status +
        "}";
    }
}
