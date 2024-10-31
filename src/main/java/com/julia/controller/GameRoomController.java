package com.julia.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.julia.model.AliveGameRo;
import com.julia.tool.PlayerToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.tool.Rv;
import com.julia.model.QueryPagement;
import com.julia.entity.GameRoomEntity;
import com.julia.service.IGameRoomService;
import org.springframework.web.bind.annotation.*;
import com.julia.model.vo.GameRoomEntityVO;

import javax.annotation.Resource;

/**
 * <p>
 * 游戏房间 前端控制器
 * </p>
 *
 * @author chowel
 * @since 2024-09-28
 */
@Api(tags = "游戏房间")
@RestController
@RequestMapping("/julia/v2/gameRoomEntity")
public class GameRoomController {
    @Resource
    IGameRoomService serviceImpl;

    @ApiOperation("分页查找")
    @PostMapping("/querywhitpage")
    public Rv<Page<GameRoomEntityVO>> queryGameRoomEntityWhitPage(@RequestBody QueryPagement queryPagement) {
        return new Rv<>(serviceImpl.findForPage(queryPagement));
    }

    @ApiOperation("根据id查找")
    @GetMapping("/query/{id}")
    public Rv<GameRoomEntityVO> getGameRoomEntityOne(@PathVariable Long id) {
        return new Rv<>(serviceImpl.findOneById(id));
    }

    @ApiOperation("根据flag查找")
    @GetMapping("/queryByFlag/{ide}")
    public Rv<GameRoomEntityVO> queryByFlag(@PathVariable String ide) {
        return new Rv<>(serviceImpl.findOneByFlag(ide));
    }

    @ApiOperation("添加")
    @PostMapping("/add")
    public Rv<GameRoomEntityVO> addGameRoomEntityOne(@RequestBody GameRoomEntityVO vo) {
        int playId = PlayerToken.getLoginIdAsInt();
        vo.setPlayerId(playId);
        return new Rv<>(serviceImpl.saveGameRoomEntity(vo));
    }

    @ApiOperation("加入房间")
    @PostMapping("/join")
    public Rv<GameRoomEntityVO> joinGameRoomEntityOne(@RequestBody GameRoomEntityVO vo) {
        int playId = PlayerToken.getLoginIdAsInt();
        vo.setPlayerId(playId);
        return new Rv<>(serviceImpl.joinGameRoomEntity(vo));
    }

    @ApiOperation("添加拉霸房间")
    @PostMapping("/addLaba")
    public Rv<GameRoomEntityVO> addLabaRoom(@RequestBody GameRoomEntityVO vo) {
        int playId = PlayerToken.getLoginIdAsInt();
        vo.setPlayerId(playId);
        return new Rv<>(serviceImpl.saveRoomByLaba(vo));
    }



    @ApiOperation("查找进行中的游戏")
    @GetMapping("/findAliveGame")
    public Rv<AliveGameRo> findAliveGame(){
        int playId = PlayerToken.getLoginIdAsInt();
        AliveGameRo ro = serviceImpl.findAliveByUserId(playId);
        if(ObjectUtils.isEmpty(ro)){
            return new Rv<>("No",ro);
        }
        return new Rv<>(ro);
    }


}

