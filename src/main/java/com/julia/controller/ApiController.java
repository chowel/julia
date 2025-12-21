package com.julia.controller;

import com.julia.model.dto.LoginDto;
import com.julia.model.game.MinaGame;
import com.julia.model.vo.GujiPlayerEntityVO;
import com.julia.model.vo.MinaSysAdminEntityVO;
import com.julia.service.IGujiPlayerService;
import com.julia.service.IMinaGameService;
import com.julia.service.IMinaSysAdminService;
import com.julia.tool.RedisUtils;
import com.julia.tool.Rv;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2023-11-01 14:49
 **/
@Log4j2
@Api(tags = "通用接口不要token")
@RestController
@RequestMapping("/guji/api")
public class ApiController {


    @Resource
    IMinaSysAdminService sysAdminService;

    @Resource
    IGujiPlayerService  gujiPlayerService;

    @Resource
    IMinaGameService gameService;

    @Resource
    RedisUtils redisUtils;

    @ApiOperation("登陆/获取token")
    @PostMapping("/login")
    public Rv<MinaSysAdminEntityVO> platLogin(@RequestBody LoginDto dto) {
        return new Rv<>(sysAdminService.login(dto));
    }

    @ApiOperation("游戏登陆")
    @PostMapping("/gameLogin")
    public Rv<GujiPlayerEntityVO> gameLogin(@RequestBody LoginDto dto) {
        return new Rv<>(gujiPlayerService.login(dto));
    }

    @ApiOperation("游戏注册")
    @PostMapping("/gameRegister")
    public Rv<GujiPlayerEntityVO> gameRegister(@RequestBody LoginDto dto) {
        return new Rv<>(gujiPlayerService.register(dto));
    }

    @ApiOperation("test")
    @GetMapping("/test")
    public Rv<String> test() {
        GujiPlayerEntityVO vo = new GujiPlayerEntityVO();
        vo.setUserId(2L);
        vo.setGuideRoot(0);
        gujiPlayerService.alter(vo);
        return new Rv<>("OK");
    }



}
