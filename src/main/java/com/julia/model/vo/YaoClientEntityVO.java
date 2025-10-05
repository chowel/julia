package com.julia.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.julia.tool.BaseEntity;

import org.springframework.beans.BeanUtils;
import com.julia.entity.YaoClientEntity;
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
 * @since 2025-10-04
 */
@Getter
@Setter
@ApiModel(value = "YaoClientEntityVO", description = "VO")
public class YaoClientEntityVO extends BaseEntity {

    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("客户编号")
    private String secure;

    @ApiModelProperty("域名")
    private String hostname;

    @ApiModelProperty("账号类型 1 电话号码  2  邮箱")
    private Integer type;

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("国家地区码")
    private String areacode;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("手机验证码")
    private String phoneVerify;

    @ApiModelProperty("交易码")
    private String trade;

    @Override
    public String toString() {
        return "{" +
            "id = " + id +
            ", secure = " + secure +
            ", hostname = " + hostname +
            ", type = " + type +
            ", account = " + account +
            ", areacode = " + areacode +
            ", password = " + password +
            ", phoneVerify = " + phoneVerify +
            ", trade = " + trade +
        "}";
    }
}
