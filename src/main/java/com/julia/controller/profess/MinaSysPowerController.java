package com.julia.controller.profess;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.MinaSysPowerEntity;
import com.julia.service.IMinaSysPowerService;
import org.springframework.web.bind.annotation.*;
import com.julia.model.vo.MinaSysPowerEntityVO;
import com.julia.model.QueryPagement;
import com.julia.tool.Rv;
import javax.annotation.Resource;

/**
 * <p>
 * 系统角色 前端控制器
 * </p>
 *
 * @author chowel
 * @since 2025-10-17
 */
@Api(tags = "系统角色")
@RestController
@RequestMapping("/guji/profess/minaSysPower")
public class MinaSysPowerController {
    @Resource
    IMinaSysPowerService serviceImpl;

    @ApiOperation("分页查找")
    @PostMapping("/querywhitpage")
    public Rv<Page<MinaSysPowerEntityVO>> queryMinaSysPowerEntityWhitPage(@RequestBody QueryPagement queryPagement) {
        return new Rv<>(serviceImpl.findForPage(queryPagement));
    }

    @ApiOperation("根据id查找")
    @GetMapping("/query/{id}")
    public Rv<MinaSysPowerEntityVO> getMinaSysPowerEntityOne(@PathVariable Long id) {
        return new Rv<>(serviceImpl.findOneById(id)); }

    @ApiOperation("添加")
    @PostMapping("/add")
    public Rv<Boolean> addMinaSysPowerEntityOne(@RequestBody MinaSysPowerEntityVO vo) {
            return new Rv<>(serviceImpl.saveMinaSysPowerEntity(vo));
    }

    @ApiOperation("修改")
    @PostMapping("/changed")
    public Rv<Boolean> changedMinaSysPowerEntityOne(@RequestBody MinaSysPowerEntityVO vo) {
        return new Rv<>(serviceImpl.alter(vo));
    }

    @ApiOperation("删除")
    @GetMapping("/del/{id}")
    public Rv<Boolean> delMinaSysPowerEntityById(@PathVariable Long id) {
          return new Rv<>(serviceImpl.remove(id));
    }
}

