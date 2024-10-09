package com.julia.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.model.QueryPagement;
import com.julia.model.dto.*;
import com.julia.model.vo.CarOrderVO;
import com.julia.model.vo.GameRoomEntityVO;
import com.julia.model.vo.YaoEntityVO;
import com.julia.service.IGameService;
import com.julia.service.IPowerService;
import com.julia.service.IRocketService;
import com.julia.service.IYaoService;
import com.julia.tool.PlayerToken;
import com.julia.tool.PokerMoldForFive;
import com.julia.tool.Rv;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2023-11-07 17:08
 **/

@Api(tags = "Play接口")
@RestController
@RequestMapping("/julia/v2/rich")
public class PowerRichController {

    @Resource
    IGameService serviceImpl;

    @ApiOperation("收牌")
    @PostMapping("/receive")
    public Rv<List<PokerMoldForFive>> receive(@RequestBody ReceivePokerDto dto) {
        dto.setPlayId(PlayerToken.getLoginIdAsInt());
        return new Rv<>(serviceImpl.receive(dto));
    }
}
