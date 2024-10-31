package com.julia.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.tool.Rv;
import com.julia.model.QueryPagement;
import com.julia.entity.GameLabaEntity;
import com.julia.service.IGameLabaService;
import org.springframework.web.bind.annotation.*;
import com.julia.model.vo.GameLabaEntityVO;

import javax.annotation.Resource;

/**
 * <p>
 * laba游戏详情 前端控制器
 * </p>
 *
 * @author chowel
 * @since 2024-10-31
 */
@Api(tags = "laba游戏详情")
@RestController
@RequestMapping("/julia/v2/gameLabaEntity")
public class GameLabaController {
    @Resource
    IGameLabaService serviceImpl;

    @ApiOperation("分页查找")
    @PostMapping("/querywhitpage")
    public Rv<Page<GameLabaEntityVO>> queryGameLabaEntityWhitPage(@RequestBody QueryPagement queryPagement) {
        return new Rv<>(serviceImpl.findForPage(queryPagement));
    }

    @ApiOperation("根据id查找")
    @GetMapping("/query/{id}")
    public Rv<GameLabaEntityVO> getGameLabaEntityOne(@PathVariable Long id) {
        return new Rv<>(serviceImpl.findOneById(id)); }

    @ApiOperation("添加")
    @PostMapping("/add")
    public Rv<Boolean> addGameLabaEntityOne(@RequestBody GameLabaEntityVO vo) {
            return new Rv<>(serviceImpl.saveGameLabaEntity(vo));
    }

    @ApiOperation("修改")
    @PostMapping("/changed")
    public Rv<Boolean> changedGameLabaEntityOne(@RequestBody GameLabaEntityVO vo) {
        return new Rv<>(serviceImpl.alter(vo));
    }

    @ApiOperation("删除")
    @GetMapping("/del/{id}")
    public Rv<Boolean> delGameLabaEntityById(@PathVariable Long id) {
          return new Rv<>(serviceImpl.remove(id));
    }
}

