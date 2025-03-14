package com.julia.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.StackPlayerEntity;
import com.julia.service.IStackPlayerService;
import org.springframework.web.bind.annotation.*;
import com.julia.model.vo.StackPlayerEntityVO;
import com.julia.model.QueryPagement;
import com.julia.tool.Rv;
import javax.annotation.Resource;

/**
 * <p>
 * 玩家 前端控制器
 * </p>
 *
 * @author chowel
 * @since 2025-03-09
 */
@Api(tags = "玩家")
@RestController
@RequestMapping("/kl/profess/player")
public class StackPlayerController {
    @Resource
    IStackPlayerService serviceImpl;

    @ApiOperation("分页查找")
    @PostMapping("/querywhitpage")
    public Rv<Page<StackPlayerEntityVO>> queryStackPlayerEntityWhitPage(@RequestBody QueryPagement queryPagement) {
        return new Rv<>(serviceImpl.findForPage(queryPagement));
    }

    @ApiOperation("根据id查找")
    @GetMapping("/query/{id}")
    public Rv<StackPlayerEntityVO> getStackPlayerEntityOne(@PathVariable Long id) {
        return new Rv<>(serviceImpl.findOneById(id)); }

//    @ApiOperation("添加")
//    @PostMapping("/add")
//    public Rv<Boolean> addStackPlayerEntityOne(@RequestBody StackPlayerEntityVO vo) {
//            return new Rv<>(serviceImpl.saveStackPlayerEntity(vo));
//    }

    @ApiOperation("修改")
    @PostMapping("/changed")
    public Rv<Boolean> changedStackPlayerEntityOne(@RequestBody StackPlayerEntityVO vo) {
        return new Rv<>(serviceImpl.alter(vo));
    }

    @ApiOperation("删除")
    @GetMapping("/del/{id}")
    public Rv<Boolean> delStackPlayerEntityById(@PathVariable Long id) {
          return new Rv<>(serviceImpl.remove(id));
    }
}

