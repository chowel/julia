package com.julia.controller.profess;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.MinaSysAdminEntity;
import com.julia.service.IMinaSysAdminService;
import org.springframework.web.bind.annotation.*;
import com.julia.model.vo.MinaSysAdminEntityVO;
import com.julia.model.QueryPagement;
import com.julia.tool.Rv;
import javax.annotation.Resource;

/**
 * <p>
 * 账号 前端控制器
 * </p>
 *
 * @author chowel
 * @since 2025-10-17
 */
@Api(tags = "账号")
@RestController
@RequestMapping("/kitano/profess/minaSys")
public class MinaSysAdminController {
    @Resource
    IMinaSysAdminService serviceImpl;

    @ApiOperation("分页查找")
    @PostMapping("/querywhitpage")
    public Rv<Page<MinaSysAdminEntityVO>> queryMinaSysAdminEntityWhitPage(@RequestBody QueryPagement queryPagement) {
        return new Rv<>(serviceImpl.findForPage(queryPagement));
    }

    @ApiOperation("根据id查找")
    @GetMapping("/query/{id}")
    public Rv<MinaSysAdminEntityVO> getMinaSysAdminEntityOne(@PathVariable Long id) {
        return new Rv<>(serviceImpl.findOneById(id)); }

    @ApiOperation("添加")
    @PostMapping("/add")
    public Rv<Boolean> addMinaSysAdminEntityOne(@RequestBody MinaSysAdminEntityVO vo) {
            return new Rv<>(serviceImpl.saveMinaSysAdminEntity(vo));
    }

    @ApiOperation("修改")
    @PostMapping("/changed")
    public Rv<Boolean> changedMinaSysAdminEntityOne(@RequestBody MinaSysAdminEntityVO vo) {
        return new Rv<>(serviceImpl.alter(vo));
    }

    @ApiOperation("删除")
    @GetMapping("/del/{id}")
    public Rv<Boolean> delMinaSysAdminEntityById(@PathVariable Long id) {
          return new Rv<>(serviceImpl.remove(id));
    }
}

