package com.julia.model;

import com.julia.model.dto.FortuneRedis;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @program: skychain
 * @description:
 * @author: Chowel.Master
 * @create: 2023-11-21 17:42
 **/

@Data
@ApiModel(value = "webSocket fortun")
public class WsFortunneBO {

    @ApiModelProperty("主题")
    private String sub;

    @ApiModelProperty("数据体")
    private List<FortuneRedis> list;
}
