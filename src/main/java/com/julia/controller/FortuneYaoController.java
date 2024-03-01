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
@Api(tags = "财神")
@RestController
@RequestMapping("/julia/profess/yao")
public class FortuneYaoController {
    @Resource
    IFortuneService serviceImpl;


    @ApiOperation("分页查找")
    @PostMapping("/queryPage")
    public Rv<FortuneWhitPageAndMetriVO> queryPage(@RequestBody QueryPagement queryPagement) {
        FortuneWhitPageAndMetriVO vo = new FortuneWhitPageAndMetriVO();
        vo.setPage(serviceImpl.queryPage(queryPagement));
        vo.setList(serviceImpl.mertric(queryPagement));
        return new Rv<>(vo);
    }

}

