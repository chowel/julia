package com.julia.controller;

import com.julia.model.dto.LoginDto;
import com.julia.model.vo.MinaSysAdminEntityVO;
import com.julia.service.IMinaSysAdminService;
import com.julia.tool.Rv;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2023-11-01 14:49
 **/
@Log4j2
@Api(tags = "通用接口不要token")
@RestController
@RequestMapping("/kitano/api")
public class ApiController {

    
    @Resource
    IMinaSysAdminService sysAdminService;

    @ApiOperation("登陆/获取token")
    @PostMapping("/login")
    public Rv<MinaSysAdminEntityVO> platLogin(@RequestBody LoginDto dto) {
        return new Rv<>(sysAdminService.login(dto));
    }

//    @ApiOperation("盘方获取token")
//    @PostMapping("/getToken")
//    public Rv<String> getToken(@RequestBody LoginDto dto) {
//        return new Rv<>(yaoService.getTokenByPan(dto));
//    }

    @ApiOperation("test")
    @GetMapping("/test")
    public Rv<String> test() {
//        Channel c  = ChannelPond.findChannel("5");
//        if(!ObjectUtils.isEmpty(c)){
//            ChannelPond.removeChannel(c);
//        }
//
        return new Rv<>("OK: ");
    }


    //   收银台接口
//    @ApiOperation("find")
//    @GetMapping("/getOne/{fortuneNo}")
//    public Rv<FortuneApiVO> getOne(@PathVariable String fortuneNo) {
//        return new Rv<>(serviceImpl.getOneByNo(fortuneNo));
//    }
//
//    //   收银台接口
//    @ApiOperation("Poll")
//    @PostMapping("/poll")
//    public Rv<Boolean> drawerPoll(@RequestBody DrawerPollDTO dto) {
//        return new Rv<>(serviceImpl.dispenseCar(dto));
//    }
//
//
//    @ApiOperation("获取收单")
//    @PostMapping("/getFortune")
//    public Rv<FortuneApiVO> getFortune(@RequestBody FortuneDTO dto) {
//        return new Rv<>(serviceImpl.findOneByNo(dto.getFortuneNo()));
//    }
//
//    @ApiOperation("发起回调")
//    @PostMapping("/handCallBack")
//    public Rv<Boolean> handCallBack(@RequestBody FortuneDTO dto) {
//        return new Rv<>(serviceImpl.callBack(dto));
//    }
//
//    @ApiOperation("创建财神")
//    @PostMapping("/createFortune")
//    public Rv<CreateFortuneVO> createFortune(@RequestBody NewFortuneDTO dto) {
//        String panId = (String) StpUtil.getLoginIdByToken(dto.getToken());
//        return new Rv<>(serviceImpl.addFortune(dto, Integer.parseInt(panId)));
//    }
}
