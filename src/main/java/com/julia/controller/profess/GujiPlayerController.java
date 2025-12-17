package com.julia.controller.profess;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.GujiPlayerEntity;
import com.julia.service.IGujiPlayerService;
import org.springframework.web.bind.annotation.*;
import com.julia.model.vo.GujiPlayerEntityVO;
import com.julia.model.QueryPagement;
import com.julia.tool.Rv;
import javax.annotation.Resource;

/**
 * <p>
 * 玩家 前端控制器
 * </p>
 *
 * @author chowel
 * @since 2025-12-17
 */
@Api(tags = "玩家")
@RestController
@RequestMapping("/guji/profess/gujiPlayerEntity")
public class GujiPlayerController {
    @Resource
    IGujiPlayerService serviceImpl;

    @ApiOperation("分页查找")
    @PostMapping("/querywhitpage")
    public Rv<Page<GujiPlayerEntityVO>> queryGujiPlayerEntityWhitPage(@RequestBody QueryPagement queryPagement) {
        return new Rv<>(serviceImpl.findForPage(queryPagement));
    }

    @ApiOperation("根据id查找")
    @GetMapping("/query/{id}")
    public Rv<GujiPlayerEntityVO> getGujiPlayerEntityOne(@PathVariable Long id) {
        return new Rv<>(serviceImpl.findOneById(id)); }

    @ApiOperation("添加")
    @PostMapping("/add")
    public Rv<Boolean> addGujiPlayerEntityOne(@RequestBody GujiPlayerEntityVO vo) {
            return new Rv<>(serviceImpl.saveGujiPlayerEntity(vo));
    }

    @ApiOperation("修改")
    @PostMapping("/changed")
    public Rv<Boolean> changedGujiPlayerEntityOne(@RequestBody GujiPlayerEntityVO vo) {
        return new Rv<>(serviceImpl.alter(vo));
    }

    @ApiOperation("删除")
    @GetMapping("/del/{id}")
    public Rv<Boolean> delGujiPlayerEntityById(@PathVariable Long id) {
          return new Rv<>(serviceImpl.remove(id));
    }
}

