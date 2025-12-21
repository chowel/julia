package com.julia.controller.profess;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.GujiPlayerWeaponEntity;
import com.julia.service.IGujiPlayerWeaponService;
import org.springframework.web.bind.annotation.*;
import com.julia.model.vo.GujiPlayerWeaponEntityVO;
import com.julia.model.QueryPagement;
import com.julia.tool.Rv;
import javax.annotation.Resource;

/**
 * <p>
 * 玩家武器 前端控制器
 * </p>
 *
 * @author chowel
 * @since 2025-12-21
 */
@Api(tags = "玩家武器")
@RestController
@RequestMapping("/guji/profess/gujiPlayerWeapon")
public class GujiPlayerWeaponController {
    @Resource
    IGujiPlayerWeaponService serviceImpl;

    @ApiOperation("分页查找")
    @PostMapping("/querywhitpage")
    public Rv<Page<GujiPlayerWeaponEntityVO>> queryGujiPlayerWeaponEntityWhitPage(@RequestBody QueryPagement queryPagement) {
        return new Rv<>(serviceImpl.findForPage(queryPagement));
    }

    @ApiOperation("根据id查找")
    @GetMapping("/query/{id}")
    public Rv<GujiPlayerWeaponEntityVO> getGujiPlayerWeaponEntityOne(@PathVariable Long id) {
        return new Rv<>(serviceImpl.findOneById(id)); }

    @ApiOperation("添加")
    @PostMapping("/add")
    public Rv<Boolean> addGujiPlayerWeaponEntityOne(@RequestBody GujiPlayerWeaponEntityVO vo) {
            return new Rv<>(serviceImpl.saveGujiPlayerWeaponEntity(vo));
    }

    @ApiOperation("修改")
    @PostMapping("/changed")
    public Rv<Boolean> changedGujiPlayerWeaponEntityOne(@RequestBody GujiPlayerWeaponEntityVO vo) {
        return new Rv<>(serviceImpl.alter(vo));
    }

    @ApiOperation("删除")
    @GetMapping("/del/{id}")
    public Rv<Boolean> delGujiPlayerWeaponEntityById(@PathVariable Long id) {
          return new Rv<>(serviceImpl.remove(id));
    }
}

