package com.julia.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.julia.entity.GameEntity;
import com.julia.mapper.ObtainMapper;
import com.julia.model.CaptchaVo;
import com.julia.model.dto.DrawerPollDTO;
import com.julia.model.dto.FortuneDTO;
import com.julia.model.dto.LoginDto;
import com.julia.model.dto.NewFortuneDTO;
import com.julia.model.vo.CreateFortuneVO;
import com.julia.model.vo.FortuneApiVO;
import com.julia.model.vo.PlayersEntityVO;
import com.julia.model.vo.YaoEntityVO;
import com.julia.service.IFortuneService;
import com.julia.service.IGameService;
import com.julia.service.IPlayersService;
import com.julia.service.IYaoService;
import com.julia.service.impl.PokerServiceImpl;
import com.julia.socket.ChannelPond;
import com.julia.tool.*;
import io.netty.channel.Channel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2023-11-01 14:49
 **/
@Log4j2
@Api(tags = "通用接口不要token")
@RestController
@RequestMapping("/julia/api")
public class ApiController {

    @Resource
    IYaoService yaoService;

    @Resource
    IPlayersService playersService;

    @Resource
    IGameService gameService;
    @Resource
    RedisUtils redisUtils;


    @ApiOperation("登陆/获取token")
    @PostMapping("/login")
    public Rv<YaoEntityVO> platLogin(@RequestBody LoginDto dto) {
        return new Rv<>(yaoService.login(dto));
    }

    @ApiOperation("登陆/获取token")
    @PostMapping("/playerLogin")
    public Rv<PlayersEntityVO> playerLogin(@RequestBody LoginDto dto) {
        return new Rv<>(playersService.login(dto));
    }


    @ApiOperation("test")
    @GetMapping("/test")
    public Rv<String> test() {

        gameService.lambdaUpdate()
                .eq(GameEntity::getGameNo,"UwBD1729008518371")
                .eq(GameEntity::getRoomFlag,"p123")
                .eq(GameEntity::getGameType,1)
                .set(GameEntity::getStatus,0).update();
        return new Rv<>("OK: ");
    }


    @ApiOperation("test")
    @GetMapping("/testPoker")
    public Rv<PokerMoldForFive> testPoker() {
        Poker p27 = new Poker(26, 14, "Diamonds", "A");
        Poker p28 = new Poker(4, 5, "Hearts", "5");
        Poker p16 = new Poker(39, 14, "Clubs", "A");
        Poker p17 = new Poker(17, 5, "Diamonds", "5");
        Poker p20 = new Poker(52, 14, "Spades", "A");

        List<Poker> FiveList = new ArrayList<>();
        FiveList.add(p27);FiveList.add(p28);FiveList.add(p16);FiveList.add(p17);FiveList.add(p20);

//        List<Poker> pokers = PokerUtils.shufflePoker();
//        List<Poker> FiveList = pokers.stream()
//                .limit(5) // 限制为前5个元素
//                .collect(Collectors.toList());

//        log.info(FiveList.toString());
        PokerMoldForFive mold= PokerUtils.generateMold(FiveList);
        if("Kicker".equals(mold.getName())){
            PokerMoldForFive straight = PokerUtils.checkStraight(FiveList);
            if(ObjectUtils.isEmpty(straight)){
                PokerMoldForFive flush = PokerUtils.checkFlush(FiveList);
                if(ObjectUtils.isEmpty(flush)){
                    return new Rv<>(mold);
                }else{
                    return new Rv<>(flush);
                }
            }else{
                return new Rv<>(straight);
            }
        }else{
            return new Rv<>(mold);
        }


    }


}
