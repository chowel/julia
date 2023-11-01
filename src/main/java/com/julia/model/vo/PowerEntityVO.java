package com.julia.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.julia.tool.BaseEntity;

import org.springframework.beans.BeanUtils;
import com.julia.entity.PowerEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

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
@ApiModel(value = "PowerEntityVO", description = "权限表VO")
public class PowerEntityVO extends BaseEntity {

    @ApiModelProperty("主键powerId")
    private Integer powerId;

    @ApiModelProperty("权限名称")
    private String powerName;

    @ApiModelProperty("权限关键字")
    private String powerKey;

    @ApiModelProperty("1:菜单 2:接口")
    private Integer powerType;

    @ApiModelProperty("排序2")
    private Integer sort;

    @ApiModelProperty("1:正常 0:删除 3")
    private Integer cheDel;

    @ApiModelProperty("备注65656")
    private String remark;

    @Override
    public String toString() {
        return "{" +
            "powerId = " + powerId +
            ", powerName = " + powerName +
            ", powerKey = " + powerKey +
            ", powerType = " + powerType +
            ", sort = " + sort +
            ", cheDel = " + cheDel +
            ", remark = " + remark +
        "}";
    }
}
