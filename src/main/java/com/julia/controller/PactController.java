package com.julia.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.julia.entity.YaoEntity;
import com.julia.service.IYaoService;
import com.julia.tool.JuliaUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.PactEntity;
import com.julia.service.IPactService;
import org.springframework.web.bind.annotation.*;
import com.julia.model.vo.PactEntityVO;
import com.julia.model.QueryPagement;
import com.julia.tool.Rv;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单/接口权限項 前端控制器
 * </p>
 *
 * @author chowel
 * @since 2023-11-01
 */
@Api(tags = "菜单/接口权限項")
@RestController
@RequestMapping("/profess/pactEntity")
public class PactController {
    @Resource
    IPactService serviceImpl;

    @Resource
    IYaoService yaoService;

    @ApiOperation("分页查找")
    @PostMapping("/querywhitpage")
    public Rv<Page<PactEntityVO>> queryPactEntityWhitPage(@RequestBody QueryPagement queryPagement) {
        return new Rv<>(serviceImpl.findForPage(queryPagement));
    }

    @ApiOperation("根据id查找")
    @GetMapping("/query/{id}")
    public Rv<PactEntityVO> getPactEntityOne(@PathVariable Long id) {
        return new Rv<>(serviceImpl.findOneById(id));
    }

    @ApiOperation("添加")
    @PostMapping("/add")
    public Rv<Boolean> addPactEntityOne(@RequestBody PactEntityVO vo) {
        return new Rv<>(serviceImpl.savePactEntity(vo));
    }

    @ApiOperation("修改")
    @PostMapping("/changed")
    public Rv<Boolean> changedPactEntityOne(@RequestBody PactEntityVO vo) {
        return new Rv<>(serviceImpl.alter(vo));
    }

    @ApiOperation("删除")
    @GetMapping("/del/{id}")
    public Rv<Boolean> delPactEntityById(@PathVariable Long id) {
        return new Rv<>(serviceImpl.remove(id));
    }

    @ApiOperation("获取用户菜单")
    @GetMapping("getMenus")
    public Rv<List<PactEntityVO>> getMenusByYaoId() {
        YaoEntity yao = yaoService.getById(StpUtil.getLoginIdAsInt());
        return new Rv<>(serviceImpl.findPactById(yao.getRoleId()));
    }

    @ApiOperation("根据powerId获取用菜单/接口数组")
    @GetMapping("getPacts")
    public Rv<List<PactEntityVO>> getPactsByPowerId(@PathVariable int powerId) {
        return new Rv<>(serviceImpl.findPactById(powerId));
    }

    @ApiOperation("获取全部用菜单/接口数组")
    @GetMapping("getAllPacts")
    public Rv<List<PactEntityVO>> getAllPacts() {
        return new Rv<>(serviceImpl.list().stream().map(e -> JuliaUtils.convertTo(new PactEntityVO(), e)).collect(Collectors.toList()));
    }
}

