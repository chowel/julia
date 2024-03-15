package com.julia.controller;

import com.julia.mapper.ObtainMapper;
import com.julia.model.CaptchaVo;
import com.julia.model.dto.DrawerPollDTO;
import com.julia.model.dto.LoginDto;
import com.julia.model.vo.FortuneApiVO;
import com.julia.model.vo.YaoEntityVO;
import com.julia.service.IFortuneService;
import com.julia.service.IYaoService;
import com.julia.service.impl.WebSocketService;
import com.julia.socket.ChannelPond;
import com.julia.tool.Captcha;
import com.julia.tool.JuliaUtils;
import com.julia.tool.Rv;
import io.netty.channel.Channel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2023-11-01 14:49
 **/
@Log4j2
@Api(tags = "通用接口不要token")
@RestController
@RequestMapping("/julia/api")
public class ApiController {

    @Resource
    IYaoService yaoService;

    @Resource
    IFortuneService serviceImpl;

    @ApiOperation("登陆/获取token")
    @PostMapping("/login")
    public Rv<YaoEntityVO> platLogin(@RequestBody LoginDto dto) {
        return new Rv<>(yaoService.login(dto));
    }

    @ApiOperation("盘方获取token")
    @PostMapping("/getToken")
    public Rv<String> getToken(@RequestBody LoginDto dto) {
        return new Rv<>(yaoService.getTokenByPan(dto));
    }

    @ApiOperation("test")
    @GetMapping("/test")
    public Rv<String> test() {
        Long n = System.currentTimeMillis();
        Long t = JuliaUtils.todayTime();
        log.info("t:{}",t-n);
        int s = (int) ((t-n)/1000);
        return new Rv<>("OK: "+s);
    }

//    @ApiOperation("addScore")
//    @GetMapping("/addScore/{userid}")
//    public Rv<String> addScore(@PathVariable String userid) {
//        obtainMapper.coutEmpty(5);
//        return new Rv<>("OK: ");
//    }

    @ApiOperation("find")
    @GetMapping("/getOne/{fortuneNo}")
    public Rv<FortuneApiVO> getOne(@PathVariable String fortuneNo) {
        return new Rv<>(serviceImpl.findOneByNo(fortuneNo));
    }
//
    @ApiOperation("Poll")
    @PostMapping("/poll")
    public Rv<Boolean> drawerPoll(@RequestBody DrawerPollDTO dto) {
        return new Rv<>(serviceImpl.gotoCar(dto));
    }
}
