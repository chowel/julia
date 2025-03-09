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
import org.springframework.util.ObjectUtils;
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
@RequestMapping("/kl/api")
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
        Channel c  = ChannelPond.findChannel("5");
        if(!ObjectUtils.isEmpty(c)){
            ChannelPond.removeChannel(c);
        }

        return new Rv<>("OK: ");
    }


    //   收银台接口
    @ApiOperation("find")
    @GetMapping("/getOne/{fortuneNo}")
    public Rv<FortuneApiVO> getOne(@PathVariable String fortuneNo) {
        return new Rv<>(serviceImpl.getOneByNo(fortuneNo));
    }

    //   收银台接口
    @ApiOperation("Poll")
    @PostMapping("/poll")
    public Rv<Boolean> drawerPoll(@RequestBody DrawerPollDTO dto) {
        return new Rv<>(serviceImpl.dispenseCar(dto));
    }


    @ApiOperation("获取收单")
    @PostMapping("/getFortune")
    public Rv<FortuneApiVO> getFortune(@RequestBody FortuneDTO dto) {
        return new Rv<>(serviceImpl.findOneByNo(dto.getFortuneNo()));
    }

    @ApiOperation("发起回调")
    @PostMapping("/handCallBack")
    public Rv<Boolean> handCallBack(@RequestBody FortuneDTO dto) {
        return new Rv<>(serviceImpl.callBack(dto));
    }

    @ApiOperation("创建财神")
    @PostMapping("/createFortune")
    public Rv<CreateFortuneVO> createFortune(@RequestBody NewFortuneDTO dto) {
        String panId = (String) StpUtil.getLoginIdByToken(dto.getToken());
        return new Rv<>(serviceImpl.addFortune(dto, Integer.parseInt(panId)));
    }
}
