package com.julia.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.model.QueryPagement;
import com.julia.model.dto.FortuneDTO;
import com.julia.model.dto.FortuneWhitPageAndMetriVO;
import com.julia.model.dto.HandOutDTO;
import com.julia.model.dto.NewFortuneDTO;
import com.julia.model.vo.CreateFortuneVO;
import com.julia.model.vo.FortuneApiVO;
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
@RequestMapping("/julia/profess/pan")
public class FortunePanController {
    @Resource
    IFortuneService serviceImpl;

    @ApiOperation("盘方分页查找")
    @PostMapping("/queryPageForPan")
    public Rv<FortuneWhitPageAndMetriVO> queryPageForPan(@RequestBody QueryPagement queryPagement) {
        Map<String, Object> sf = queryPagement.getSearchFields();
        sf.put("panid",StpUtil.getLoginIdAsInt());
        queryPagement.setSearchFields(sf);

        FortuneWhitPageAndMetriVO vo = new FortuneWhitPageAndMetriVO();
        vo.setPage(serviceImpl.queryPage(queryPagement));
        vo.setList(serviceImpl.mertric(queryPagement));
        return new Rv<>(vo);
    }

//    @ApiOperation("newFortune")
//    @PostMapping("/createFortune")
//    public Rv<CreateFortuneVO> createFortune(@RequestBody NewFortuneDTO dto) {
//        return new Rv<>(serviceImpl.addFortune(dto,StpUtil.getLoginIdAsInt()));
//    }

//    @ApiOperation("财神")
//    @PostMapping("/fortuneIn")
//    public Rv<String> fortuneByDeposit(@RequestBody FortuneDTO dto) {
//        return new Rv<>(serviceImpl.handIn(dto,StpUtil.getLoginIdAsInt()));
//    }

//    @ApiOperation("获取收单")
//    @PostMapping("/getFortune")
//    public Rv<FortuneApiVO> getFortune(@RequestBody FortuneDTO dto) {
//        return new Rv<>(serviceImpl.findOneByNo(dto.getFortuneNo()));
//    }


//    @ApiOperation("发起回调")
//    @PostMapping("/handCallBack")
//    public Rv<Boolean> handCallBack(@RequestBody FortuneDTO dto) {
//        return new Rv<>(serviceImpl.callBack(dto,StpUtil.getLoginIdAsInt()));
//    }
}

