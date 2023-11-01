package com.julia.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.RocketEntity;
import com.julia.service.IRocketService;
import org.springframework.web.bind.annotation.*;
import com.julia.model.vo.RocketEntityVO;
import com.julia.model.QueryPagement;
import com.julia.tool.Rv;
import javax.annotation.Resource;

/**
 * <p>
 * 火箭业务 前端控制器
 * </p>
 *
 * @author chowel
 * @since 2023-11-01
 */
@Api(tags = "火箭业务")
@RestController
@RequestMapping("/profess/rocketEntity")
public class RocketController {
    @Resource
    IRocketService serviceImpl;

    @ApiOperation("分页查找")
    @PostMapping("/querywhitpage")
    public Rv<Page<RocketEntityVO>> queryRocketEntityWhitPage(@RequestBody QueryPagement queryPagement) {
        return new Rv<>(serviceImpl.findForPage(queryPagement));
    }

    @ApiOperation("根据id查找")
    @GetMapping("/query/{id}")
    public Rv<RocketEntityVO> getRocketEntityOne(@PathVariable Long id) {
        return new Rv<>(serviceImpl.findOneById(id)); }

    @ApiOperation("添加")
    @PostMapping("/add")
    public Rv<Boolean> addRocketEntityOne(@RequestBody RocketEntityVO vo) {
            return new Rv<>(serviceImpl.saveRocketEntity(vo));
    }

    @ApiOperation("修改")
    @PostMapping("/changed")
    public Rv<Boolean> changedRocketEntityOne(@RequestBody RocketEntityVO vo) {
        return new Rv<>(serviceImpl.alter(vo));
    }

    @ApiOperation("删除")
    @GetMapping("/del/{id}")
    public Rv<Boolean> delRocketEntityById(@PathVariable Long id) {
          return new Rv<>(serviceImpl.remove(id));
    }
}

