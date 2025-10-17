package com.julia.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.julia.tool.BaseEntity;

import org.springframework.beans.BeanUtils;
import com.julia.entity.MinaSysMenuEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 系统菜单
 * </p>
 *
 * @author chowel
 * @since 2025-10-17
 */
@Getter
@Setter
@ApiModel(value = "MinaSysMenuEntityVO", description = "系统菜单VO")
public class MinaSysMenuEntityVO extends BaseEntity {

    @ApiModelProperty("主键id")
    private Integer pactId;

    @ApiModelProperty("名称")
    private String pactName;

    @ApiModelProperty("父id")
    private Integer parentId;

    @ApiModelProperty("1:菜单大类 2:菜单項 3:接口")
    private Integer pactType;

    @ApiModelProperty("接口关键字 3有")
    private String perms;

    @ApiModelProperty("菜单路由 2有")
    private String url;

    @ApiModelProperty("菜单图标2有")
    private String icon;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("是否删除 1 正常 0 删除")
    private Integer cheDel;

    @Override
    public String toString() {
        return "{" +
            "pactId = " + pactId +
            ", pactName = " + pactName +
            ", parentId = " + parentId +
            ", pactType = " + pactType +
            ", perms = " + perms +
            ", url = " + url +
            ", icon = " + icon +
            ", sort = " + sort +
            ", cheDel = " + cheDel +
        "}";
    }
}
