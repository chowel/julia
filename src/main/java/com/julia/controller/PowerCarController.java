package com.julia.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.model.QueryPagement;
import com.julia.model.dto.CarOperaDTO;
import com.julia.model.vo.CarOrderVO;
import com.julia.model.vo.RocketEntityVO;
import com.julia.service.IObtainService;
import com.julia.service.IRocketService;
import com.julia.tool.Rv;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2023-11-07 17:08
 **/

@Api(tags = "车队接口")
@RestController
@RequestMapping("/julia/profess/car")
public class PowerCarController {

    @Resource
    IRocketService serviceImpl;

    @Resource
    IObtainService obtainService;

    @ApiOperation("分页查找")
    @PostMapping("/querywhitpage")
    public Rv<Page<CarOrderVO>> queryRocketEntityWhitPage(@RequestBody QueryPagement queryPagement) {
        return new Rv<>(serviceImpl.findForPage(queryPagement));
    }

    @ApiOperation("订单操作")
    @PostMapping("/carOpera")
    public Rv<Boolean> carOpera(@RequestBody CarOperaDTO dto) {
        dto.setCId(StpUtil.getLoginIdAsInt());
        return new Rv<>(serviceImpl.carOpera(dto));
    }

    @ApiOperation("查找完成订单byId")
    @PostMapping("/queryRocketById")
    public Rv<Page<CarOrderVO>> queryRocketById(@RequestBody QueryPagement queryPagement) {
        Map<String,Object> searchFields = queryPagement.getSearchFields();
        searchFields.put("cId",StpUtil.getLoginIdAsInt());
        return new Rv<>(serviceImpl.findForPage(queryPagement));
    }

}
