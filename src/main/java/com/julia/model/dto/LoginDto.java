package com.julia.model.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;


@Data
@ApiModel(value = "LoginDto", description = "登录")
public class LoginDto {

    @ApiModelProperty(value = "用户名")
    @NotEmpty(message = "用户名不能为空")
    private String name;

    @ApiModelProperty(value = "密码")
    @NotEmpty(message = "密码不能为空")
    private String password;


    @ApiModelProperty(value = "验证码")
    private String validation;
}
