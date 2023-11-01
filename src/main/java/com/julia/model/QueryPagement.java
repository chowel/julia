package com.julia.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @program: skychain
 * @description:
 * @author: Chowel.Master
 * @create: 2022-10-19 09:30
 **/
@ApiModel(value = "QueryPagement",description = "分页封装DTO")
@Data
public class QueryPagement {
    /**
     * 开始页码
     */
    @ApiModelProperty(value = "开始页",example = "0")
    private Integer startPage = 1;
    /**
     * 每页容量
     */
    @ApiModelProperty(value = "每页容量",example = "10")
    private Integer pageSize = 10;
    /**
     * 降序字段
     */
    @ApiModelProperty(value = "降序字段",example = " ",allowEmptyValue = true)
    private List<String> descFields;
    /**
     * 升序字段
     */
    @ApiModelProperty(value = "升序字段",example = " ",allowEmptyValue = true)
    private List<String> ascFields;
    /**
     * 搜索字段
     */
    @ApiModelProperty(value = "搜索字段",example = " ",allowEmptyValue = true)
    private Map<String,Object> searchFields;
    /**
     * 字典表大类
     */
    @ApiModelProperty(value = "字典表大类",example = " ",allowEmptyValue = true)
    private List<String> distType;
}
