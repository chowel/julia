package com.julia.model.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;


@Data
@ApiModel(value = "MidPasswordDto", description = "修改密码dto")
public class MidPasswordDto {

    @ApiModelProperty(value = "yaoId")
    private Integer yaoId;

    @ApiModelProperty(value = "旧密码")
    @NotEmpty(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "新密码")
    private String newPassword;
}
