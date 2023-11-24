package com.julia.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: skychain
 * @description:
 * @author: Chowel.Master
 * @create: 2023-11-21 17:42
 **/

@Data
@ApiModel(value = "webSocket 消息通讯 DTO")
public class WebSocketMsgBO {

    @ApiModelProperty("主题")
    private String sub;

    @ApiModelProperty("数据体")
    private Object data;
}
