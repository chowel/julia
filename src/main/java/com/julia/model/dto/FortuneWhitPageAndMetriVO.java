package com.julia.model.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.MetricsFortuneEntity;
import com.julia.model.vo.FortuneEntityVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @program: julia
 * @description: 收单分页及统计
 * @author: Chowel.Master
 * @create: 2024-02-22 15:37
 **/
@Getter
@Setter
@ApiModel(value = "FortuneWhitPageAndMetriVO", description = "收单分页及统计")
public class FortuneWhitPageAndMetriVO {
    @ApiModelProperty("分页")
    private Page<FortuneEntityVO> page;

    @ApiModelProperty("统计")
    private List<MetricsFortuneEntity> list;
}
