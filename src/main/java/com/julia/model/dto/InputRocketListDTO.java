package com.julia.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2023-11-08 11:46
 **/
@Getter
@Setter
@ApiModel(value = "InputRocketList", description = "输入数组")
public class InputRocketListDTO {
    List<InputRocketDTO>  list;
}
