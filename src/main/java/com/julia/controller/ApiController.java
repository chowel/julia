package com.julia.controller;

import com.julia.model.CaptchaVo;
import com.julia.model.dto.LoginDto;
import com.julia.model.vo.YaoEntityVO;
import com.julia.service.IYaoService;
import com.julia.tool.Captcha;
import com.julia.tool.Rv;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2023-11-01 14:49
 **/

@Api(tags = "通用接口不要token")
@RestController
@RequestMapping("/julia/api")
public class ApiController {

    @Resource
    IYaoService yaoService;

    @ApiOperation("登陆/获取token")
    @PostMapping("/login")
    public Rv<YaoEntityVO> platLogin(@RequestBody LoginDto dto) {
        return new Rv<>(yaoService.login(dto));
    }

    @ApiOperation("test")
    @GetMapping("/test")
    public Rv<CaptchaVo> test() {
        try {
            CaptchaVo vo = Captcha.createCode();
            return new Rv<>(vo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
