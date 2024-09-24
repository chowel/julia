package com.julia.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.tool.Rv;
import com.julia.model.QueryPagement;
import com.julia.entity.PlayersEntity;
import com.julia.service.IPlayersService;
import org.springframework.web.bind.annotation.*;
import com.julia.model.vo.PlayersEntityVO;

import javax.annotation.Resource;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author chowel
 * @since 2024-09-24
 */
@Api(tags = "用户表")
@RestController
@RequestMapping("/julia/v2/playersEntity")
public class PlayersController {
    @Resource
    IPlayersService serviceImpl;

    @ApiOperation("分页查找")
    @PostMapping("/querywhitpage")
    public Rv<Page<PlayersEntityVO>> queryPlayersEntityWhitPage(@RequestBody QueryPagement queryPagement) {
        return new Rv<>(serviceImpl.findForPage(queryPagement));
    }

    @ApiOperation("根据id查找")
    @GetMapping("/query/{id}")
    public Rv<PlayersEntityVO> getPlayersEntityOne(@PathVariable Long id) {
        return new Rv<>(serviceImpl.findOneById(id)); }

    @ApiOperation("添加")
    @PostMapping("/add")
    public Rv<Boolean> addPlayersEntityOne(@RequestBody PlayersEntityVO vo) {
            return new Rv<>(serviceImpl.savePlayersEntity(vo));
    }

    @ApiOperation("修改")
    @PostMapping("/changed")
    public Rv<Boolean> changedPlayersEntityOne(@RequestBody PlayersEntityVO vo) {
        return new Rv<>(serviceImpl.alter(vo));
    }

    @ApiOperation("删除")
    @GetMapping("/del/{id}")
    public Rv<Boolean> delPlayersEntityById(@PathVariable Long id) {
          return new Rv<>(serviceImpl.remove(id));
    }
}

