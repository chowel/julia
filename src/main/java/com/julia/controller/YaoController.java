package com.julia.controller;

import com.julia.model.vo.PactEntityVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.YaoEntity;
import com.julia.service.IYaoService;
import org.springframework.web.bind.annotation.*;
import com.julia.model.vo.YaoEntityVO;
import com.julia.model.QueryPagement;
import com.julia.tool.Rv;
import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 账号 前端控制器
 * </p>
 *
 * @author chowel
 * @since 2023-11-01
 */
@Api(tags = "账号")
@RestController
@RequestMapping("/profess/yaoEntity")
public class YaoController {
    @Resource
    IYaoService serviceImpl;

    @ApiOperation("分页查找")
    @PostMapping("/querywhitpage")
    public Rv<Page<YaoEntityVO>> queryYaoEntityWhitPage(@RequestBody QueryPagement queryPagement) {
        return new Rv<>(serviceImpl.findForPage(queryPagement));
    }

    @ApiOperation("根据id查找")
    @GetMapping("/query/{id}")
    public Rv<YaoEntityVO> getYaoEntityOne(@PathVariable Long id) {
        return new Rv<>(serviceImpl.findOneById(id)); }

    @ApiOperation("添加")
    @PostMapping("/add")
    public Rv<Boolean> addYaoEntityOne(@RequestBody YaoEntityVO vo) {
            return new Rv<>(serviceImpl.saveYaoEntity(vo));
    }

    @ApiOperation("修改")
    @PostMapping("/changed")
    public Rv<Boolean> changedYaoEntityOne(@RequestBody YaoEntityVO vo) {
        return new Rv<>(serviceImpl.alter(vo));
    }

    @ApiOperation("删除")
    @GetMapping("/del/{id}")
    public Rv<Boolean> delYaoEntityById(@PathVariable Long id) {
          return new Rv<>(serviceImpl.remove(id));
    }

}

