package com.julia.controller;

import com.julia.model.dto.DrawerPollDTO;
import com.julia.model.vo.GameOrderEntityVO;
import com.julia.model.vo.PayConfigEntityVO;
import com.julia.service.IGameOrderService;
import com.julia.service.IPayConfigService;
import com.julia.service.impl.AlipayService;
import com.julia.tool.PlayerToken;
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
 * @create: 2025-03-15 12:19
 **/

@Log4j2
@Api(tags = "游戏前端接口")
@RestController
@RequestMapping("/kl/game")
public class GameController {
    @Resource
    IPayConfigService payConfigService;

    @Resource
    AlipayService alipayService;

    @Resource
    IGameOrderService gameOrderService;

    @ApiOperation("获取支付配置")
    @GetMapping("/getpayments")
    public Rv<List<PayConfigEntityVO>> getpayments() {
        return new Rv<>(payConfigService.allconfig());
    }


    @ApiOperation("预添加订单")
    @PostMapping("/preAddOrder")
    public Rv<DrawerPollDTO> preAddOrder(@RequestBody GameOrderEntityVO vo) {
        vo.setPlayerId(PlayerToken.getLoginIdAsInt());
        return new Rv<>(gameOrderService.preCreateOrder(vo));
    }




}
