package com.julia.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.tool.Rv;
import com.julia.model.QueryPagement;
import com.julia.entity.GameThirteenEntity;
import com.julia.service.IGameThirteenService;
import org.springframework.web.bind.annotation.*;
import com.julia.model.vo.GameThirteenEntityVO;

import javax.annotation.Resource;

/**
 * <p>
 * 13游戏详情 前端控制器
 * </p>
 *
 * @author chowel
 * @since 2024-10-10
 */
@Api(tags = "13游戏详情")
@RestController
@RequestMapping("/julia/v2/gameThirteenEntity")
public class GameThirteenController {
    @Resource
    IGameThirteenService serviceImpl;

    @ApiOperation("分页查找")
    @PostMapping("/querywhitpage")
    public Rv<Page<GameThirteenEntityVO>> queryGameThirteenEntityWhitPage(@RequestBody QueryPagement queryPagement) {
        return new Rv<>(serviceImpl.findForPage(queryPagement));
    }

    @ApiOperation("根据id查找")
    @GetMapping("/query/{id}")
    public Rv<GameThirteenEntityVO> getGameThirteenEntityOne(@PathVariable Long id) {
        return new Rv<>(serviceImpl.findOneById(id)); }

    @ApiOperation("添加")
    @PostMapping("/add")
    public Rv<Boolean> addGameThirteenEntityOne(@RequestBody GameThirteenEntityVO vo) {
            return new Rv<>(serviceImpl.saveGameThirteenEntity(vo));
    }

    @ApiOperation("修改")
    @PostMapping("/changed")
    public Rv<Boolean> changedGameThirteenEntityOne(@RequestBody GameThirteenEntityVO vo) {
        return new Rv<>(serviceImpl.alter(vo));
    }

    @ApiOperation("删除")
    @GetMapping("/del/{id}")
    public Rv<Boolean> delGameThirteenEntityById(@PathVariable Long id) {
          return new Rv<>(serviceImpl.remove(id));
    }
}

