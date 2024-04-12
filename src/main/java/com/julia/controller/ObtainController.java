package com.julia.controller;

import cn.dev33.satoken.stp.StpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.ObtainEntity;
import com.julia.service.IObtainService;
import org.springframework.web.bind.annotation.*;
import com.julia.model.vo.ObtainEntityVO;
import com.julia.model.QueryPagement;
import com.julia.tool.Rv;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 车队下账号 前端控制器
 * </p>
 *
 * @author chowel
 * @since 2024-02-23
 */
@Api(tags = "车队下账号")
@RestController
@RequestMapping("/julia/profess/obtainEntity")
public class ObtainController {
    @Resource
    IObtainService serviceImpl;

    @ApiOperation("分页查找")
    @PostMapping("/querywhitpage")
    public Rv<Page<ObtainEntityVO>> queryObtainEntityWhitPage(@RequestBody QueryPagement queryPagement) {
        Map<String, Object> sf = queryPagement.getSearchFields();
        sf.put("carid",StpUtil.getLoginIdAsInt());
        queryPagement.setSearchFields(sf);
        return new Rv<>(serviceImpl.findForPage(queryPagement));
    }

    @ApiOperation("根据id查找")
    @GetMapping("/query/{id}")
    public Rv<ObtainEntityVO> getObtainEntityOne(@PathVariable Long id) {
        return new Rv<>(serviceImpl.findOneById(id));
    }

    @ApiOperation("添加")
    @PostMapping("/add")
    public Rv<Boolean> addObtainEntityOne(@RequestBody ObtainEntityVO vo) {
        vo.setYaoId(StpUtil.getLoginIdAsInt());
        return new Rv<>(serviceImpl.saveObtainEntity(vo));
    }

    @ApiOperation("修改")
    @PostMapping("/changed")
    public Rv<Boolean> changedObtainEntityOne(@RequestBody ObtainEntityVO vo) {
        return new Rv<>(serviceImpl.alter(vo));
    }

    @ApiOperation("删除")
    @GetMapping("/del/{id}")
    public Rv<Boolean> delObtainEntityById(@PathVariable Integer id) {
        return new Rv<>(serviceImpl.remove(id));
    }

    @ApiOperation("查找全部")
    @PostMapping("/findbyCar")
    public Rv<List<ObtainEntityVO>> findbyCar() {
        return new Rv<>(serviceImpl.allObtain(StpUtil.getLoginIdAsInt()));
    }

    @ApiOperation("清空计数")
    @GetMapping("/clear")
    public Rv<Boolean> clear() {
        return new Rv<>(serviceImpl.clearCout(StpUtil.getLoginIdAsInt()));
    }
}

