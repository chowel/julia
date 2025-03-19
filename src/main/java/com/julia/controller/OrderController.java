package com.julia.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.OrderEntity;
import com.julia.service.IOrderService;
import org.springframework.web.bind.annotation.*;
import com.julia.model.vo.OrderEntityVO;
import com.julia.model.QueryPagement;
import com.julia.tool.Rv;
import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author chowel
 * @since 2025-03-19
 */
@Api(tags = "")
@RestController
@RequestMapping("/profess/orderEntity")
public class OrderController {
    @Resource
    IOrderService serviceImpl;

    @ApiOperation("分页查找")
    @PostMapping("/querywhitpage")
    public Rv<Page<OrderEntityVO>> queryOrderEntityWhitPage(@RequestBody QueryPagement queryPagement) {
        return new Rv<>(serviceImpl.findForPage(queryPagement));
    }

    @ApiOperation("根据id查找")
    @GetMapping("/query/{id}")
    public Rv<OrderEntityVO> getOrderEntityOne(@PathVariable Long id) {
        return new Rv<>(serviceImpl.findOneById(id)); }

//    @ApiOperation("添加")
//    @PostMapping("/add")
//    public Rv<Boolean> addOrderEntityOne(@RequestBody OrderEntityVO vo) {
//            return new Rv<>(serviceImpl.saveOrderEntity(vo));
//    }

    @ApiOperation("修改")
    @PostMapping("/changed")
    public Rv<Boolean> changedOrderEntityOne(@RequestBody OrderEntityVO vo) {
        return new Rv<>(serviceImpl.alter(vo));
    }

    @ApiOperation("删除")
    @GetMapping("/del/{id}")
    public Rv<Boolean> delOrderEntityById(@PathVariable Long id) {
          return new Rv<>(serviceImpl.remove(id));
    }
}

