package com.julia.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.julia.tool.BaseEntity;

import org.springframework.beans.BeanUtils;
import com.julia.entity.ObtainEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 车队下账号
 * </p>
 *
 * @author chowel
 * @since 2024-02-29
 */
@Getter
@Setter
@ApiModel(value = "ObtainEntityVO", description = "车队下账号VO")
public class ObtainEntityVO extends BaseEntity {

    @ApiModelProperty("主键id")
    private Integer obtainId;

    @ApiModelProperty("关联车队id")
    private Integer yaoId;

    @ApiModelProperty("账号")
    private String name;

    @ApiModelProperty("姓")
    private String firstName;

    @ApiModelProperty("名")
    private String lastName;

    @ApiModelProperty("状态 0 停用 1启用")
    private Integer status;

    @ApiModelProperty("每日使用次数")
    private Integer cout;

    @ApiModelProperty("备注")
    private String note;

    @Override
    public String toString() {
        return "{" +
            "obtainId = " + obtainId +
            ", yaoId = " + yaoId +
            ", name = " + name +
            ", firstName = " + firstName +
            ", lastName = " + lastName +
            ", status = " + status +
            ", cout = " + cout +
            ", note = " + note +
        "}";
    }
}
