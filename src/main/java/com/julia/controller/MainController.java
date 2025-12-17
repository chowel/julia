package com.julia.controller;

import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2025-10-17 14:20
 **/

@Log4j2
@Api(tags = "通用接口不要token")
@RestController
@RequestMapping("/guji/v1")
public class MainController {
}
