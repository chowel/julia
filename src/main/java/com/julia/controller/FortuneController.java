package com.julia.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.julia.model.dto.FortuneDTO;
import com.julia.model.dto.InputRocketDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.FortuneEntity;
import com.julia.service.IFortuneService;
import org.springframework.web.bind.annotation.*;
import com.julia.model.vo.FortuneEntityVO;
import com.julia.model.QueryPagement;
import com.julia.tool.Rv;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * <p>
 * 财神 前端控制器
 * </p>
 *
 * @author chowel
 * @since 2024-02-15
 */
@Api(tags = "财神")
@RestController
@RequestMapping("/julia/profess/fortuneEntity")
public class FortuneController {
    @Resource
    IFortuneService serviceImpl;

    @ApiOperation("分页查找")
    @PostMapping("/querywhitpage")
    public Rv<Page<FortuneEntityVO>> queryFortuneEntityWhitPage(@RequestBody QueryPagement queryPagement) {
        return new Rv<>(serviceImpl.findForPage(queryPagement));
    }

    @ApiOperation("根据id查找")
    @GetMapping("/query/{id}")
    public Rv<FortuneEntityVO> getFortuneEntityOne(@PathVariable Long id) {
        return new Rv<>(serviceImpl.findOneById(id)); }

    @ApiOperation("添加")
    @PostMapping("/add")
    public Rv<Boolean> addFortuneEntityOne(@RequestBody FortuneEntityVO vo) {
            return new Rv<>(serviceImpl.saveFortuneEntity(vo));
    }

    @ApiOperation("修改")
    @PostMapping("/changed")
    public Rv<Boolean> changedFortuneEntityOne(@RequestBody FortuneEntityVO vo) {
        return new Rv<>(serviceImpl.alter(vo));
    }

    @ApiOperation("删除")
    @GetMapping("/del/{id}")
    public Rv<Boolean> delFortuneEntityById(@PathVariable Long id) {
          return new Rv<>(serviceImpl.remove(id));
    }

    @ApiOperation("财神")
    @PostMapping("/fortuneInput")
    public Rv<Boolean> fortuneByDeposit(@RequestBody FortuneDTO dto) {
        return new Rv<>(serviceImpl.input(dto,StpUtil.getLoginIdAsInt()));
    }
}

