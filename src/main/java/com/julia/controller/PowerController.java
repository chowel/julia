package com.julia.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.PowerEntity;
import com.julia.service.IPowerService;
import org.springframework.web.bind.annotation.*;
import com.julia.model.vo.PowerEntityVO;
import com.julia.model.QueryPagement;
import com.julia.tool.Rv;
import javax.annotation.Resource;

/**
 * <p>
 * 权限表 前端控制器
 * </p>
 *
 * @author chowel
 * @since 2023-11-01
 */
@Api(tags = "权限表")
@RestController
@RequestMapping("/profess/powerEntity")
public class PowerController {
    @Resource
    IPowerService serviceImpl;

    @ApiOperation("分页查找")
    @PostMapping("/querywhitpage")
    public Rv<Page<PowerEntityVO>> queryPowerEntityWhitPage(@RequestBody QueryPagement queryPagement) {
        return new Rv<>(serviceImpl.findForPage(queryPagement));
    }

    @ApiOperation("根据id查找")
    @GetMapping("/query/{id}")
    public Rv<PowerEntityVO> getPowerEntityOne(@PathVariable Long id) {
        return new Rv<>(serviceImpl.findOneById(id)); }

    @ApiOperation("添加")
    @PostMapping("/add")
    public Rv<Boolean> addPowerEntityOne(@RequestBody PowerEntityVO vo) {
            return new Rv<>(serviceImpl.savePowerEntity(vo));
    }

    @ApiOperation("修改")
    @PostMapping("/changed")
    public Rv<Boolean> changedPowerEntityOne(@RequestBody PowerEntityVO vo) {
        return new Rv<>(serviceImpl.alter(vo));
    }

    @ApiOperation("删除")
    @GetMapping("/del/{id}")
    public Rv<Boolean> delPowerEntityById(@PathVariable Long id) {
          return new Rv<>(serviceImpl.remove(id));
    }
}

