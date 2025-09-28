package com.julia.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.CoinLogEntity;
import com.julia.service.ICoinLogService;
import org.springframework.web.bind.annotation.*;
import com.julia.model.vo.CoinLogEntityVO;
import com.julia.model.QueryPagement;
import com.julia.tool.Rv;
import javax.annotation.Resource;

/**
 * <p>
 * 上货记录 前端控制器
 * </p>
 *
 * @author chowel
 * @since 2024-03-04
 */
@Api(tags = "上货记录")
@RestController
@RequestMapping("/yoki/profess/coinLog")
public class CoinLogController {
    @Resource
    ICoinLogService serviceImpl;

    @ApiOperation("分页查找")
    @PostMapping("/querywhitpage")
    public Rv<Page<CoinLogEntityVO>> queryCoinLogEntityWhitPage(@RequestBody QueryPagement queryPagement) {
        return new Rv<>(serviceImpl.findForPage(queryPagement));
    }

    @ApiOperation("根据id查找")
    @GetMapping("/query/{id}")
    public Rv<CoinLogEntityVO> getCoinLogEntityOne(@PathVariable Long id) {
        return new Rv<>(serviceImpl.findOneById(id)); }

//    @ApiOperation("添加")
//    @PostMapping("/add")
//    public Rv<Boolean> addCoinLogEntityOne(@RequestBody CoinLogEntityVO vo) {
//            return new Rv<>(serviceImpl.saveCoinLogEntity(vo));
//    }
//
//    @ApiOperation("修改")
//    @PostMapping("/changed")
//    public Rv<Boolean> changedCoinLogEntityOne(@RequestBody CoinLogEntityVO vo) {
//        return new Rv<>(serviceImpl.alter(vo));
//    }

//    @ApiOperation("删除")
//    @GetMapping("/del/{id}")
//    public Rv<Boolean> delCoinLogEntityById(@PathVariable Long id) {
//          return new Rv<>(serviceImpl.remove(id));
//    }
}

