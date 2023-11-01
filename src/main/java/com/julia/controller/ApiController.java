package com.julia.controller;

import com.julia.model.dto.LoginDto;
import com.julia.model.vo.YaoEntityVO;
import com.julia.service.IYaoService;
import com.julia.tool.Rv;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2023-11-01 14:49
 **/

@Api(tags = "通用接口不要token")
@RestController
@RequestMapping("/julia/api")
public class ApiController {

    @Resource
    IYaoService yaoService;

    @ApiOperation("登陆/获取token")
    @PostMapping("/login")
    public Rv<YaoEntityVO> platLogin(@RequestBody LoginDto dto) {
        return new Rv<>(yaoService.login(dto));
    }


}
