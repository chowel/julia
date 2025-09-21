package com.julia.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.julia.model.QueryPagement;
import com.julia.model.vo.GameOrderEntityVO;
import com.julia.model.vo.PayConfigEntityVO;
import com.julia.model.vo.StackPlayerEntityVO;
import com.julia.service.IGameOrderService;
import com.julia.service.IPayConfigService;
import com.julia.service.IStackPlayerService;
import com.julia.service.impl.HuiYuanService;
import com.julia.tool.Rv;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2025-03-16 17:30
 **/

@Api(tags = "游戏后台")
@RestController
@RequestMapping("/kl/profess")
public class ProfessController {

    @Resource
    IStackPlayerService serviceImpl;

    @Resource
    IPayConfigService payConfigService;

    @Resource
    IGameOrderService gameOrderService;

    @Resource
    HuiYuanService huiYuanService;

    @ApiOperation("获取支付配置")
    @GetMapping("/professPayments")
    public Rv<List<PayConfigEntityVO>> professPayments() {
        return new Rv<>(payConfigService.allconfig());
    }


    @ApiOperation("添加")
    @PostMapping("/addPayment")
    public Rv<Boolean> addPayment(@RequestBody PayConfigEntityVO vo) {
        return new Rv<>(payConfigService.savePayConfigEntity(vo));
    }

    @ApiOperation("修改")
    @PostMapping("/updatePayment")
    public Rv<Boolean> changedPayConfigEntityOne(@RequestBody PayConfigEntityVO vo) {
        return new Rv<>(payConfigService.alter(vo));
    }

    @ApiOperation("删除")
    @GetMapping("/delPayment/{id}")
    public Rv<Boolean> delPayConfigEntityById(@PathVariable Long id) {
        return new Rv<>(payConfigService.remove(id));
    }

    @ApiOperation("分页查找")
    @PostMapping("/queryPlayerwhitpage")
    public Rv<Page<StackPlayerEntityVO>> queryPlayerwhitpage(@RequestBody QueryPagement queryPagement) {
        return new Rv<>(serviceImpl.findForPage(queryPagement));
    }

    @ApiOperation("分页查找支付订单")
    @PostMapping("/queryOrder")
    public Rv<Page<GameOrderEntityVO>> queryOrder(@RequestBody QueryPagement queryPagement) {
        return new Rv<>(gameOrderService.findForPage(queryPagement));
    }


    @ApiOperation("订单详情")
    @PostMapping("/queryOrderOne")
    public Rv<Boolean> queryOrderOne(@RequestBody GameOrderEntityVO vo) {
        return new Rv<>(huiYuanService.getDetailOrder(vo.getOutOrderNo(),vo.getOrderNo(),vo.getPlayerName()));
    }
}
