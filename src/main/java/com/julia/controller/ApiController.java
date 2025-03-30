package com.julia.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.alipay.api.internal.util.AlipaySignature;
import com.julia.mapper.ObtainMapper;
import com.julia.model.CaptchaVo;
import com.julia.model.alipay.CallbackParam;
import com.julia.model.dto.DrawerPollDTO;
import com.julia.model.dto.FortuneDTO;
import com.julia.model.dto.LoginDto;
import com.julia.model.dto.NewFortuneDTO;
import com.julia.model.vo.CreateFortuneVO;
import com.julia.model.vo.FortuneApiVO;
import com.julia.model.vo.StackPlayerEntityVO;
import com.julia.model.vo.YaoEntityVO;
import com.julia.service.IFortuneService;
import com.julia.service.IStackPlayerService;
import com.julia.service.IYaoService;
import com.julia.service.impl.WebSocketService;
import com.julia.socket.ChannelPond;
import com.julia.tool.Captcha;
import com.julia.tool.JuliaUtils;
import com.julia.tool.Rv;
import io.netty.channel.Channel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

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

    @Resource
    IStackPlayerService playerService;

    @ApiOperation("登陆/获取token")
    @PostMapping("/login")
    public Rv<YaoEntityVO> platLogin(@RequestBody LoginDto dto) {
        return new Rv<>(yaoService.login(dto));
    }

    @ApiOperation("玩家登录")
    @PostMapping("/playerLogin")
    public Rv<StackPlayerEntityVO> playerLogin(@RequestBody LoginDto dto) {
        return new Rv<>(playerService.playerLogin(dto));
    }

    @ApiOperation("玩家注册")
    @PostMapping("/playerSignin")
    public Rv<Boolean> playerSignin(@RequestBody LoginDto dto) {
        return new Rv<>(playerService.saveStackPlayerEntity(dto));
    }

    @ApiOperation("回调函数")
    @GetMapping("/callBack")
    public String alipayCallBack(CallbackParam param) {
        log.info(param.getNotify_id());
        return "success";
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
//    @ApiOperation("find")
//    @GetMapping("/getOne/{fortuneNo}")
//    public Rv<FortuneApiVO> getOne(@PathVariable String fortuneNo) {
//        return new Rv<>(serviceImpl.getOneByNo(fortuneNo));
//    }

    //   收银台接口
//    @ApiOperation("Poll")
//    @PostMapping("/poll")
//    public Rv<Boolean> drawerPoll(@RequestBody DrawerPollDTO dto) {
//        return new Rv<>(serviceImpl.dispenseCar(dto));
//    }


//    @ApiOperation("获取收单")
//    @PostMapping("/getFortune")
//    public Rv<FortuneApiVO> getFortune(@RequestBody FortuneDTO dto) {
//        return new Rv<>(serviceImpl.findOneByNo(dto.getFortuneNo()));
//    }
//
@SneakyThrows
@ApiOperation("支付宝订单支付回调")
@PostMapping("/callback")
public String callback(HttpServletRequest request) {
    log.info("支付宝订单支付回调");
    Map<String, String> params = new HashMap<String, String>();
    Map requestParams = request.getParameterMap();
    for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
        String name = (String) iter.next();
        String[] values = (String[]) requestParams.get(name);
        String valueStr = "";
        for (int i = 0; i < values.length; i++) {
            valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
        }
        //乱码解决，这段代码在出现乱码时使用。
        //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
        params.put(name, valueStr);
    }
//    boolean flag = AlipaySignature.rsaCertCheckV1(params, alipayCertPublicKey, "UTF-8", "RSA2");

//    if (flag) {
//        logger.info("验签通过");
//        alipayService.handleCallBack(params);
//        return "success";
//    }
    return "fail";
}
//
//    @ApiOperation("创建财神")
//    @PostMapping("/createFortune")
//    public Rv<CreateFortuneVO> createFortune(@RequestBody NewFortuneDTO dto) {
//        String panId = (String) StpUtil.getLoginIdByToken(dto.getToken());
//        return new Rv<>(serviceImpl.addFortune(dto, Integer.parseInt(panId)));
//    }
}
