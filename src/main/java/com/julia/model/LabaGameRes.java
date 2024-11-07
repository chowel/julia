package com.julia.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @program: julia
 * @description: 拉霸结果
 * @author: Chowel.Master
 * @create: 2024-10-31 17:22
 **/
@Getter
@Setter
@ApiModel(value = "LabaGameRes", description = "拉霸结果")
public class LabaGameRes {
    @ApiModelProperty("房间ide")
    private String roomFlag;

    @ApiModelProperty("游戏")
    private String gameNo;

    @ApiModelProperty("开奖结果")
    private List<Integer> res;
}
