package com.julia.controller;

import com.julia.model.dto.LoginDto;
import com.julia.model.vo.MinaSysAdminEntityVO;
import com.julia.service.IMinaSysAdminService;
import com.julia.tool.Rv;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2023-11-01 14:49
 **/
@Log4j2
@Api(tags = "通用接口不要token")
@RestController
@RequestMapping("/kitano/api")
public class ApiController {


    @Resource
    IMinaSysAdminService sysAdminService;

    @ApiOperation("登陆/获取token")
    @PostMapping("/login")
    public Rv<MinaSysAdminEntityVO> platLogin(@RequestBody LoginDto dto) {
        return new Rv<>(sysAdminService.login(dto));
    }

//    @ApiOperation("盘方获取token")
//    @PostMapping("/getToken")
//    public Rv<String> getToken(@RequestBody LoginDto dto) {
//        return new Rv<>(yaoService.getTokenByPan(dto));
//    }

    @ApiOperation("test")
    @GetMapping("/test")
    public Rv<String> test() {
//        Channel c  = ChannelPond.findChannel("5");
//        if(!ObjectUtils.isEmpty(c)){
//            ChannelPond.removeChannel(c);
//        }
//
        return new Rv<>("OK: ");
    }




}
