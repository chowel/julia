package com.julia.controller;

import com.julia.config.PlayerToken;
import com.julia.entity.GujiPlayerEntity;
import com.julia.model.dto.LoginDto;
import com.julia.model.vo.GujiPlayerEntityVO;
import com.julia.service.IGujiPlayerService;
import com.julia.tool.JuliaUtils;
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
 * @create: 2025-10-17 14:20
 **/

@Log4j2
@Api(tags = "通用接口不要token")
@RestController
@RequestMapping("/guji/v1")
public class MainController {

    @Resource
    IGujiPlayerService gujiPlayerService;

    @ApiOperation("获取用户数据")
    @GetMapping("/getPlayer")
    public Rv<GujiPlayerEntityVO> getPlayer() {
        Long userId = PlayerToken.getLoginIdAsLong();
        GujiPlayerEntityVO vo = gujiPlayerService.findOneById(userId);
        vo.setUserId(0L);
        vo.setPassword("****");
        return new Rv<>(vo);
    }

    @ApiOperation("更新用户数据")
    @PostMapping("/updatePlayer")
    public Rv<Boolean> updatePlayer(@RequestBody GujiPlayerEntityVO vo) {
        Long userId = PlayerToken.getLoginIdAsLong();
        vo.setUserId(userId);
        return new Rv<>(gujiPlayerService.alter(vo));
    }
}
