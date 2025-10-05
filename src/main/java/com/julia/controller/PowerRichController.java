package com.julia.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.model.QueryPagement;
import com.julia.model.dto.CarOperaDTO;
import com.julia.model.dto.InputRocketDTO;
import com.julia.model.dto.InputRocketListDTO;
import com.julia.model.dto.MidPasswordDto;
import com.julia.model.vo.CarOrderVO;
import com.julia.model.vo.YaoEntityVO;
import com.julia.service.IPowerService;
import com.julia.service.IRocketService;
import com.julia.service.IYaoService;
import com.julia.tool.Rv;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2023-11-07 17:08
 **/

@Api(tags = "盘方接口")
@RestController
@RequestMapping("/yoki/profess/rich")
public class PowerRichController {

    @Resource
    IRocketService serviceImpl;

    @Resource
    IYaoService yaoService;

    @ApiOperation("分页查找")
    @PostMapping("/queryRich")
    public Rv<Page<CarOrderVO>> queryRich(@RequestBody QueryPagement queryPagement) {
        Map<String, Object> searchFields = queryPagement.getSearchFields();
        searchFields.put("pId", StpUtil.getLoginIdAsInt());
        return new Rv<>(serviceImpl.findForPage(queryPagement));
    }

    @ApiOperation("批量输入")
    @PostMapping("/inputRockets")
    public Rv<Boolean> inputRockets(@RequestBody InputRocketListDTO list) {
        return new Rv<>(serviceImpl.inputRocketBatch(list, StpUtil.getLoginIdAsInt()));
    }

    @ApiOperation("单挑输入")
    @PostMapping("/inputRocket")
    public Rv<Boolean> inputRocket(@RequestBody InputRocketDTO dto) {
        return new Rv<>(serviceImpl.inputRocket(dto, StpUtil.getLoginIdAsInt()));
    }

    @ApiOperation("收银")
    @PostMapping("/rocketByDeposit")
    public Rv<Boolean> rocketByDeposit(@RequestParam("file") MultipartFile file, InputRocketDTO dto) {
        dto.setPId(StpUtil.getLoginIdAsInt());
        return new Rv<>(serviceImpl.Deposit(file, dto));
    }

    @ApiOperation("订单查询")
    @GetMapping("/queryRocket/{orderId}")
    public Rv<CarOrderVO> carOpera(@PathVariable("orderId") String orderId) {
        return new Rv<>(serviceImpl.queryRocketByOrderId(orderId, StpUtil.getLoginIdAsInt()));
    }

    @ApiOperation("发起订单回调")
    @GetMapping("/notification/{orderId}")
    public Rv<Boolean> notification(@PathVariable("orderId") String orderId) {
        return new Rv<>(serviceImpl.noticeRocketByOrderId(orderId, StpUtil.getLoginIdAsInt()));
    }

    @ApiOperation("个人信息")
    @GetMapping("/querySelf")
    public Rv<YaoEntityVO> querySelf() {
        return new Rv<>(yaoService.mySelf(StpUtil.getLoginIdAsInt()));
    }

    @ApiOperation("生成token")
    @GetMapping("/getToken")
    public Rv<String> getToken() {
        return new Rv<>(yaoService.createToken(StpUtil.getLoginIdAsInt()));
    }

    @ApiOperation("修改")
    @PostMapping("/changed")
    public Rv<Boolean> changedYaoEntityOne(@RequestBody YaoEntityVO vo) {
        return new Rv<>(yaoService.alter(vo));
    }

    @ApiOperation("修改密码")
    @PostMapping("/changedpw")
    public Rv<Boolean> changedpw(@RequestBody MidPasswordDto dto) {
        dto.setYaoId(StpUtil.getLoginIdAsInt());
        return new Rv<>(yaoService.alterPassword(dto));
    }
}
