package com.julia.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.model.QueryPagement;
import com.julia.model.dto.CarOperaDTO;
import com.julia.model.vo.CarOrderVO;
import com.julia.model.vo.RocketEntityVO;
import com.julia.service.IRocketService;
import com.julia.tool.Rv;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2023-11-07 17:08
 **/

@Api(tags = "车队接口")
@RestController
@RequestMapping("/julia/car")
public class PowerCarController {

    @Resource
    IRocketService serviceImpl;

    @ApiOperation("分页查找")
    @PostMapping("/querywhitpage")
    public Rv<Page<CarOrderVO>> queryRocketEntityWhitPage(@RequestBody QueryPagement queryPagement) {
        return new Rv<>(serviceImpl.findForPage(queryPagement));
    }

    @ApiOperation("分页查找")
    @PostMapping("/carOpera")
    public Rv<Boolean> carOpera(@RequestBody CarOperaDTO dto) {
        dto.setCId(StpUtil.getLoginIdAsInt());
        return new Rv<>(serviceImpl.carOpera(dto));
    }
}
