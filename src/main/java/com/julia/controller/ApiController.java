package com.julia.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.julia.mapper.ObtainMapper;
import com.julia.model.CaptchaVo;
import com.julia.model.dto.DrawerPollDTO;
import com.julia.model.dto.FortuneDTO;
import com.julia.model.dto.LoginDto;
import com.julia.model.dto.NewFortuneDTO;
import com.julia.model.vo.CreateFortuneVO;
import com.julia.model.vo.FortuneApiVO;
import com.julia.model.vo.PlayersEntityVO;
import com.julia.model.vo.YaoEntityVO;
import com.julia.service.IFortuneService;
import com.julia.service.IPlayersService;
import com.julia.service.IYaoService;
import com.julia.service.impl.PokerServiceImpl;
import com.julia.socket.ChannelPond;
import com.julia.tool.Captcha;
import com.julia.tool.JuliaUtils;
import com.julia.tool.Poker;
import com.julia.tool.Rv;
import io.netty.channel.Channel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;
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
    IPlayersService playersService;

    @Resource
    PokerServiceImpl pokerService;



    @ApiOperation("登陆/获取token")
    @PostMapping("/login")
    public Rv<YaoEntityVO> platLogin(@RequestBody LoginDto dto) {
        return new Rv<>(yaoService.login(dto));
    }

    @ApiOperation("登陆/获取token")
    @PostMapping("/playerLogin")
    public Rv<PlayersEntityVO> playerLogin(@RequestBody LoginDto dto) {
        return new Rv<>(playersService.login(dto));
    }



    @ApiOperation("test")
    @GetMapping("/test")
    public Rv<String> test() {
        Channel c  = ChannelPond.findChannel("5");
        if(!ObjectUtils.isEmpty(c)){
            ChannelPond.removeChannel(c);
        }

        return new Rv<>("OK: ");
    }


    @ApiOperation("test")
    @GetMapping("/testPoker")
    public Rv<Map<String,List<Poker>>> testPoker() {

        return new Rv<>(pokerService.oneHanderThirteen());
    }



}
