package com.julia.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.julia.tool.BaseEntity;

import org.springframework.beans.BeanUtils;
import com.julia.entity.PactEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * <p>
 * 菜单/接口权限項
 * </p>
 *
 * @author chowel
 * @since 2023-11-01
 */
@Getter
@Setter
@ApiModel(value = "PactEntityVO", description = "菜单/接口权限項VO")
public class PactEntityVO extends BaseEntity {

    @ApiModelProperty("主键pactId")
    private Integer pactId;

    @ApiModelProperty("菜单/接口名称")
    private String pactName;

    @ApiModelProperty("父id")
    private Integer parentId;

    @ApiModelProperty("1:菜单大类 2:菜单項 3:接口")
    private Integer pactType;

    @ApiModelProperty("接口关键字 3才有")
    private String perms;

    @ApiModelProperty("菜单关键字 2才有")
    private String url;

    @ApiModelProperty("排序1")
    private Integer sort;

    @ApiModelProperty("图标 2才有")
    private String icon;

    @ApiModelProperty("0:删除 1:正常 555")
    private Integer cheDel;

    @ApiModelProperty("子菜单")
    private List<PactEntity> childern;

    @Override
    public String toString() {
        return "{" +
            "pactId = " + pactId +
            ", pactName = " + pactName +
            ", parentId = " + parentId +
            ", pactType = " + pactType +
            ", perms = " + perms +
            ", url = " + url +
            ", sort = " + sort +
            ", icon = " + icon +
            ", cheDel = " + cheDel +
        "}";
    }
}
