package com.julia.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.YaoClientEntity;
import com.julia.service.IYaoClientService;
import org.springframework.web.bind.annotation.*;
import com.julia.model.vo.YaoClientEntityVO;
import com.julia.model.QueryPagement;
import com.julia.tool.Rv;
import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author chowel
 * @since 2025-10-04
 */
@Api(tags = "")
@RestController
@RequestMapping("/yoki/profess/client")
public class YaoClientController {
    @Resource
    IYaoClientService serviceImpl;

    @ApiOperation("分页查找")
    @PostMapping("/querywhitpage")
    public Rv<Page<YaoClientEntityVO>> queryYaoClientEntityWhitPage(@RequestBody QueryPagement queryPagement) {
        return new Rv<>(serviceImpl.findForPage(queryPagement));
    }

    @ApiOperation("根据id查找")
    @GetMapping("/query/{id}")
    public Rv<YaoClientEntityVO> getYaoClientEntityOne(@PathVariable Long id) {
        return new Rv<>(serviceImpl.findOneById(id)); }

    @ApiOperation("添加")
    @PostMapping("/add")
    public Rv<Boolean> addYaoClientEntityOne(@RequestBody YaoClientEntityVO vo) {
            return new Rv<>(serviceImpl.saveYaoClientEntity(vo));
    }

    @ApiOperation("修改")
    @PostMapping("/changed")
    public Rv<Boolean> changedYaoClientEntityOne(@RequestBody YaoClientEntityVO vo) {
        return new Rv<>(serviceImpl.alter(vo));
    }

    @ApiOperation("删除")
    @GetMapping("/del/{id}")
    public Rv<Boolean> delYaoClientEntityById(@PathVariable Long id) {
          return new Rv<>(serviceImpl.remove(id));
    }
}

