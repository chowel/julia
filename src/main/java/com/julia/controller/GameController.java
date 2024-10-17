package com.julia.controller;

import com.julia.model.AliveGameRo;
import com.julia.tool.PlayerToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.tool.Rv;
import com.julia.model.QueryPagement;
import com.julia.entity.GameEntity;
import com.julia.service.IGameService;
import org.springframework.web.bind.annotation.*;
import com.julia.model.vo.GameEntityVO;

import javax.annotation.Resource;

/**
 * <p>
 * 游戏 前端控制器
 * </p>
 *
 * @author chowel
 * @since 2024-10-03
 */
@Api(tags = "游戏")
@RestController
@RequestMapping("/julia/v2/gameEntity")
public class GameController {
    @Resource
    IGameService serviceImpl;

    @ApiOperation("分页查找")
    @PostMapping("/querywhitpage")
    public Rv<Page<GameEntityVO>> queryGameEntityWhitPage(@RequestBody QueryPagement queryPagement) {
        return new Rv<>(serviceImpl.findForPage(queryPagement));
    }

    @ApiOperation("根据id查找")
    @GetMapping("/query/{id}")
    public Rv<GameEntityVO> getGameEntityOne(@PathVariable Long id) {
        return new Rv<>(serviceImpl.findOneById(id)); }

    @ApiOperation("添加")
    @PostMapping("/add")
    public Rv<Boolean> addGameEntityOne(@RequestBody GameEntityVO vo) {
            return new Rv<>(serviceImpl.saveGameEntity(vo));
    }

    @ApiOperation("修改")
    @PostMapping("/changed")
    public Rv<Boolean> changedGameEntityOne(@RequestBody GameEntityVO vo) {
        return new Rv<>(serviceImpl.alter(vo));
    }

    @ApiOperation("删除")
    @GetMapping("/del/{id}")
    public Rv<Boolean> delGameEntityById(@PathVariable Long id) {
          return new Rv<>(serviceImpl.remove(id));
    }

    @ApiOperation("开始新游戏")
    @PostMapping("/reThirteenGame")
    public Rv<Boolean> rePlayThirteenGame(@RequestBody GameEntityVO vo){
        int playId = PlayerToken.getLoginIdAsInt();
        return new Rv<>(serviceImpl.rePlayThirteennGame(playId,vo.getGameType(),vo.getRoomFlag()));
    }
}

