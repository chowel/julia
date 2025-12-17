package com.julia.controller.profess;

import com.julia.config.AdminToken;
import com.julia.entity.MinaSysAdminEntity;
import com.julia.service.IMinaSysAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.service.IMinaSysMenuService;
import org.springframework.web.bind.annotation.*;
import com.julia.model.vo.MinaSysMenuEntityVO;
import com.julia.model.QueryPagement;
import com.julia.tool.Rv;
import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 系统菜单 前端控制器
 * </p>
 *
 * @author chowel
 * @since 2025-10-17
 */
@Api(tags = "系统菜单")
@RestController
@RequestMapping("/guji/profess/minaSysMenu")
public class MinaSysMenuController {
    @Resource
    IMinaSysMenuService serviceImpl;

    @Resource
    IMinaSysAdminService adminService;

    @ApiOperation("分页查找")
    @PostMapping("/querywhitpage")
    public Rv<Page<MinaSysMenuEntityVO>> queryMinaSysMenuEntityWhitPage(@RequestBody QueryPagement queryPagement) {
        return new Rv<>(serviceImpl.findForPage(queryPagement));
    }

    @ApiOperation("根据id查找")
    @GetMapping("/query/{id}")
    public Rv<MinaSysMenuEntityVO> getMinaSysMenuEntityOne(@PathVariable Long id) {
        return new Rv<>(serviceImpl.findOneById(id)); }

    @ApiOperation("添加")
    @PostMapping("/add")
    public Rv<Boolean> addMinaSysMenuEntityOne(@RequestBody MinaSysMenuEntityVO vo) {
            return new Rv<>(serviceImpl.saveMinaSysMenuEntity(vo));
    }

    @ApiOperation("修改")
    @PostMapping("/changed")
    public Rv<Boolean> changedMinaSysMenuEntityOne(@RequestBody MinaSysMenuEntityVO vo) {
        return new Rv<>(serviceImpl.alter(vo));
    }

    @ApiOperation("删除")
    @GetMapping("/del/{id}")
    public Rv<Boolean> delMinaSysMenuEntityById(@PathVariable Long id) {
          return new Rv<>(serviceImpl.remove(id));
    }


    @ApiOperation("获取用户菜单")
    @GetMapping("getMenus")
    public Rv<List<MinaSysMenuEntityVO>> getMenusByYaoId() {
        MinaSysAdminEntity admin = adminService.getById(AdminToken.getLoginIdAsInt());
        return new Rv<>(serviceImpl.findMenusById(admin.getRoleId()));
    }

    @ApiOperation("根据powerId获取用菜单/接口数组")
    @GetMapping("getPacts")
    public Rv<List<MinaSysMenuEntityVO>> getPactsByPowerId(@PathVariable int powerId) {
        return new Rv<>(serviceImpl.findMenusById(powerId));
    }
}

