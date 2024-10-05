package com.julia.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.tool.Rv;
import com.julia.model.QueryPagement;
import com.julia.entity.GameScoreEntity;
import com.julia.service.IGameScoreService;
import org.springframework.web.bind.annotation.*;
import com.julia.model.vo.GameScoreEntityVO;

import javax.annotation.Resource;

/**
 * <p>
 * 游戏得分 前端控制器
 * </p>
 *
 * @author chowel
 * @since 2024-10-03
 */
@Api(tags = "游戏得分")
@RestController
@RequestMapping("/julia/v2/gameScoreEntity")
public class GameScoreController {
    @Resource
    IGameScoreService serviceImpl;

    @ApiOperation("分页查找")
    @PostMapping("/querywhitpage")
    public Rv<Page<GameScoreEntityVO>> queryGameScoreEntityWhitPage(@RequestBody QueryPagement queryPagement) {
        return new Rv<>(serviceImpl.findForPage(queryPagement));
    }

    @ApiOperation("根据id查找")
    @GetMapping("/query/{id}")
    public Rv<GameScoreEntityVO> getGameScoreEntityOne(@PathVariable Long id) {
        return new Rv<>(serviceImpl.findOneById(id)); }

    @ApiOperation("添加")
    @PostMapping("/add")
    public Rv<Boolean> addGameScoreEntityOne(@RequestBody GameScoreEntityVO vo) {
            return new Rv<>(serviceImpl.saveGameScoreEntity(vo));
    }

    @ApiOperation("修改")
    @PostMapping("/changed")
    public Rv<Boolean> changedGameScoreEntityOne(@RequestBody GameScoreEntityVO vo) {
        return new Rv<>(serviceImpl.alter(vo));
    }

    @ApiOperation("删除")
    @GetMapping("/del/{id}")
    public Rv<Boolean> delGameScoreEntityById(@PathVariable Long id) {
          return new Rv<>(serviceImpl.remove(id));
    }
}

