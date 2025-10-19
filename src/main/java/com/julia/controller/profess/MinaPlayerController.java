package com.julia.controller.profess;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.MinaPlayerEntity;
import com.julia.service.IMinaPlayerService;
import org.springframework.web.bind.annotation.*;
import com.julia.model.vo.MinaPlayerEntityVO;
import com.julia.model.QueryPagement;
import com.julia.tool.Rv;
import javax.annotation.Resource;

/**
 * <p>
 * 玩家 前端控制器
 * </p>
 *
 * @author chowel
 * @since 2025-10-17
 */
@Api(tags = "玩家")
@RestController
@RequestMapping("/kitano/profess/minaPlayerEntity")
public class MinaPlayerController {
    @Resource
    IMinaPlayerService serviceImpl;

    @ApiOperation("分页查找")
    @PostMapping("/querywhitpage")
    public Rv<Page<MinaPlayerEntityVO>> queryMinaPlayerEntityWhitPage(@RequestBody QueryPagement queryPagement) {
        return new Rv<>(serviceImpl.findForPage(queryPagement));
    }

    @ApiOperation("根据id查找")
    @GetMapping("/query/{id}")
    public Rv<MinaPlayerEntityVO> getMinaPlayerEntityOne(@PathVariable Long id) {
        return new Rv<>(serviceImpl.findOneById(id)); }

    @ApiOperation("添加")
    @PostMapping("/add")
    public Rv<Boolean> addMinaPlayerEntityOne(@RequestBody MinaPlayerEntityVO vo) {
            return new Rv<>(serviceImpl.saveMinaPlayerEntity(vo));
    }

    @ApiOperation("修改")
    @PostMapping("/changed")
    public Rv<Boolean> changedMinaPlayerEntityOne(@RequestBody MinaPlayerEntityVO vo) {
        return new Rv<>(serviceImpl.alter(vo));
    }

    @ApiOperation("删除")
    @GetMapping("/del/{id}")
    public Rv<Boolean> delMinaPlayerEntityById(@PathVariable Long id) {
          return new Rv<>(serviceImpl.remove(id));
    }
}

