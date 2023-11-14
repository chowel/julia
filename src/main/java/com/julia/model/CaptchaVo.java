package com.julia.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2023-11-14 15:28
 **/

@Getter
@Setter
@ApiModel(value = "CaptchaVo", description = "验证码vo")
public class CaptchaVo {
    @ApiModelProperty("验证码Id")
    private String captchaId;

    @ApiModelProperty("验证码结果")
    private String captchaRes;

    @ApiModelProperty("图片base64")
    private String base;
}
