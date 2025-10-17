package com.julia.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.julia.tool.BaseEntity;

import org.springframework.beans.BeanUtils;
import com.julia.entity.MinaSysPowerEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 系统角色
 * </p>
 *
 * @author chowel
 * @since 2025-10-17
 */
@Getter
@Setter
@ApiModel(value = "MinaSysPowerEntityVO", description = "系统角色VO")
public class MinaSysPowerEntityVO extends BaseEntity {

    @ApiModelProperty("主键id")
    private Integer powerId;

    @ApiModelProperty("权限名称")
    private String powerName;

    @ApiModelProperty("权限关键字")
    private String powerKey;

    @ApiModelProperty("1:菜单 2:接口")
    private Integer powerType;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("1:正常 0:删除")
    private Integer cheDel;

    @ApiModelProperty("备注")
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
