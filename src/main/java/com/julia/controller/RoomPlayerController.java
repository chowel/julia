package com.julia.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.tool.Rv;
import com.julia.model.QueryPagement;
import com.julia.entity.RoomPlayerEntity;
import com.julia.service.IRoomPlayerService;
import org.springframework.web.bind.annotation.*;
import com.julia.model.vo.RoomPlayerEntityVO;

import javax.annotation.Resource;

/**
 * <p>
 * 房间玩家 前端控制器
 * </p>
 *
 * @author chowel
 * @since 2024-09-30
 */
@Api(tags = "房间玩家")
@RestController
@RequestMapping("/julia/v2/roomPlayerEntity")
public class RoomPlayerController {
    @Resource
    IRoomPlayerService serviceImpl;

    @ApiOperation("分页查找")
    @PostMapping("/querywhitpage")
    public Rv<Page<RoomPlayerEntityVO>> queryRoomPlayerEntityWhitPage(@RequestBody QueryPagement queryPagement) {
        return new Rv<>(serviceImpl.findForPage(queryPagement));
    }

    @ApiOperation("根据id查找")
    @GetMapping("/query/{id}")
    public Rv<RoomPlayerEntityVO> getRoomPlayerEntityOne(@PathVariable Long id) {
        return new Rv<>(serviceImpl.findOneById(id)); }

    @ApiOperation("添加")
    @PostMapping("/add")
    public Rv<Boolean> addRoomPlayerEntityOne(@RequestBody RoomPlayerEntityVO vo) {
            return new Rv<>(serviceImpl.saveRoomPlayerEntity(vo));
    }

    @ApiOperation("修改")
    @PostMapping("/changed")
    public Rv<Boolean> changedRoomPlayerEntityOne(@RequestBody RoomPlayerEntityVO vo) {
        return new Rv<>(serviceImpl.alter(vo));
    }

    @ApiOperation("删除")
    @GetMapping("/del/{id}")
    public Rv<Boolean> delRoomPlayerEntityById(@PathVariable Long id) {
          return new Rv<>(serviceImpl.remove(id));
    }
}

