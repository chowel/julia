package com.julia.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.alipay.api.internal.util.AlipaySignature;
import com.julia.entity.StackPlayerEntity;
import com.julia.entity.alipaymodel.AlipayResposeVO;
import com.julia.entity.alipaymodel.PayByAliPay;
import com.julia.mapper.ObtainMapper;
import com.julia.model.CaptchaVo;
import com.julia.model.alipay.CallbackParam;
import com.julia.model.dto.DrawerPollDTO;
import com.julia.model.dto.FortuneDTO;
import com.julia.model.dto.LoginDto;
import com.julia.model.dto.NewFortuneDTO;
import com.julia.model.vo.*;
import com.julia.service.IFortuneService;
import com.julia.service.IStackPlayerService;
import com.julia.service.IYaoService;
import com.julia.service.impl.AlipayService;
import com.julia.service.impl.WebSocketService;
import com.julia.socket.ChannelPond;
import com.julia.tool.Captcha;
import com.julia.tool.JuliaUtils;
import com.julia.tool.PlayerToken;
import com.julia.tool.Rv;
import io.netty.channel.Channel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
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
    IStackPlayerService playerService;

    @Resource
    AlipayService alipayService;

    @Value("${alipay.alipayCertPath}")
    private String alipayCertPublicKey;

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

    @ApiOperation("订单查询")
    @PostMapping("/queryAlipayOrder")
    public Rv<AlipayResposeVO> queryAlipayOrder(@RequestBody PayByAliPay vo) {
        return new Rv<>(alipayService.queryPay(vo));
    }


    @ApiOperation("订单退款")
    @PostMapping("/refundAlipayOrder")
    public Rv<Boolean> refundAlipayOrder(@RequestBody PayByAliPay vo) {
        return new Rv<>(alipayService.refundPay(vo));
    }

    @ApiOperation("test")
    @GetMapping("/test")
    public Rv<String> test() {
//        Channel c = ChannelPond.findChannel("5");
//        if (!ObjectUtils.isEmpty(c)) {
//            ChannelPond.removeChannel(c);
//        }
//        StackPlayerEntity entity =  playerService.findPlayerForPay();
        playerService.addCoin(23L,1000);
        return new Rv<>("OK");
    }

    @ApiOperation("添加订单")
    @PostMapping("/pay")
    public Rv<DrawerPollDTO> createOrderByPay(@RequestBody PayByAliPay vo) {
        return new Rv<>(alipayService.savePay(vo));
    }

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
            log.info("Name: {}  Value:{}", name, valueStr);
            params.put(name, valueStr);
        }
    boolean flag = AlipaySignature.rsaCertCheckV1(params, alipayCertPublicKey, "UTF-8", "RSA2");

    if (flag) {
        log.info("回调验签通过");
        alipayService.handleCallBack(params);
        return "success";
    }
        return "fail";
    }

    @ApiOperation("创建订单")
    @PostMapping("/addOrder")
    public Rv<DrawerPollDTO> addOrder(@RequestBody GameOrderEntityVO vo) {
        return new Rv<>(alipayService.savePayFormGame(vo.getOrderNo()));
    }

    @ApiOperation("发起回调")
    @PostMapping("/sendCallback")
    public Rv<Boolean> sendCallback(@RequestBody PayByAliPay vo) {
        return new Rv<>(alipayService.reqCallBack(vo));
    }
}
