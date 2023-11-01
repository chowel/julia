package com.julia.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.julia.tool.BaseEntity;

import org.springframework.beans.BeanUtils;
import com.julia.entity.YaoEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * <p>
 * 账号
 * </p>
 *
 * @author chowel
 * @since 2023-11-01
 */
@Getter
@Setter
@ApiModel(value = "YaoEntityVO", description = "账号VO")
public class YaoEntityVO extends BaseEntity {

    @ApiModelProperty("用户表主键id")
    private Integer yaoId;

    @ApiModelProperty("登录名")
    private String loginName;

    @ApiModelProperty("用户名/昵称")
    private String userName;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("电话号码")
    private String phone;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("1:正常 2:删除/停用")
    private Integer cheDel;

    @ApiModelProperty("菜单 power_id")
    private Integer roleId;

    @ApiModelProperty("接口权限 power_id")
    private Integer authId;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("token")
    private String token;

    @ApiModelProperty("菜单组")
    private List<PactEntityVO> menus;

    @Override
    public String toString() {
        return "{" +
            "yaoId = " + yaoId +
            ", loginName = " + loginName +
            ", userName = " + userName +
            ", email = " + email +
            ", phone = " + phone +
            ", avatar = " + avatar +
            ", password = " + password +
            ", cheDel = " + cheDel +
            ", roleId = " + roleId +
            ", authId = " + authId +
            ", remark = " + remark +
        "}";
    }
}
