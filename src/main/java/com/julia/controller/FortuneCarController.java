package com.julia.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.model.QueryPagement;
import com.julia.model.dto.FortuneDTO;
import com.julia.model.dto.FortuneWhitPageAndMetriVO;
import com.julia.model.dto.HandOutDTO;
import com.julia.model.vo.FortuneEntityVO;
import com.julia.service.IFortuneService;
import com.julia.tool.Rv;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 财神 前端控制器
 * </p>
 *
 * @author chowel
 * @since 2024-02-15
 */
@Api(tags = "财神-车队")
@RestController
@RequestMapping("/julia/profess/car")
public class FortuneCarController {
    @Resource
    IFortuneService serviceImpl;


    @ApiOperation("车队分页查找")
    @PostMapping("/queryPageForCar")
    public Rv<FortuneWhitPageAndMetriVO> queryPageForCar(@RequestBody QueryPagement queryPagement) {
        Map<String, Object> sf = queryPagement.getSearchFields();
        sf.put("carid", StpUtil.getLoginIdAsInt());
        queryPagement.setSearchFields(sf);

        FortuneWhitPageAndMetriVO vo = new FortuneWhitPageAndMetriVO();
        vo.setPage(serviceImpl.queryPage(queryPagement));
        vo.setList(serviceImpl.mertric(queryPagement));
        return new Rv<>(vo);
    }


//    @ApiOperation("分页查找")
//    @PostMapping("/queryPage")
//    public Rv<FortuneWhitPageAndMetriVO> queryPage(@RequestBody QueryPagement queryPagement) {
//        FortuneWhitPageAndMetriVO vo = new FortuneWhitPageAndMetriVO();
//        vo.setPage(serviceImpl.queryPage(queryPagement));
//        vo.setList(serviceImpl.mertric(queryPagement));
//        return new Rv<>(vo);
//    }

//    @ApiOperation("根据id查找")
//    @GetMapping("/query/{id}")
//    public Rv<FortuneEntityVO> getFortuneEntityOne(@PathVariable Long id) {
//        return new Rv<>(serviceImpl.findOneById(id));
//    }

    @ApiOperation("主动下线")
    @GetMapping("/offLine/{id}")
    public Rv<FortuneEntityVO> offLine(@PathVariable Long id) {
        return new Rv<>(serviceImpl.findOneById(id));
    }

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


    @ApiOperation("车队分发收款账号")
    @PostMapping("/handOut")
    public Rv<Boolean> handOut(@RequestBody HandOutDTO dto) {
        return new Rv<>(serviceImpl.handOut(dto, StpUtil.getLoginIdAsInt()));
    }

    @ApiOperation("财神操作")
    @PostMapping("/handleFortune")
    public Rv<Boolean> fortuneBus(@RequestBody FortuneEntityVO dto) {
        return new Rv<>(serviceImpl.overFortune(dto));
    }

    @ApiOperation("拒绝处理财神")
    @PostMapping("/refuse")
    public Rv<Boolean> handRefuse(@RequestBody FortuneDTO dto) {
        return new Rv<>(serviceImpl.refuse(dto, StpUtil.getLoginIdAsInt()));
    }


}

