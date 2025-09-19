package com.julia.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.alipay.api.internal.util.AlipaySignature;
import com.julia.entity.GameOrderEntity;
import com.julia.entity.PlayerRes;
import com.julia.entity.StackPlayerEntity;
import com.julia.entity.alipaymodel.AlipayResposeVO;
import com.julia.entity.alipaymodel.PayByAliPay;
import com.julia.enums.RedisKeyEnum;
import com.julia.mapper.ObtainMapper;
import com.julia.model.CaptchaVo;
import com.julia.model.HuiYuan.BillTable;
import com.julia.model.alipay.CallbackParam;
import com.julia.model.dto.DrawerPollDTO;
import com.julia.model.dto.FortuneDTO;
import com.julia.model.dto.LoginDto;
import com.julia.model.dto.NewFortuneDTO;
import com.julia.model.vo.*;
import com.julia.service.IFortuneService;
import com.julia.service.IGameOrderService;
import com.julia.service.IStackPlayerService;
import com.julia.service.IYaoService;
import com.julia.service.impl.AlipayService;
import com.julia.service.impl.HuiYuanService;
import com.julia.service.impl.WebSocketService;
import com.julia.socket.ChannelPond;
import com.julia.tool.*;
import io.netty.channel.Channel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2023-11-01 14:49
 **/
@Log4j2
@Api(tags = "通用接口不要token")
@RestController
@RequestMapping("/kl/api")
public class ApiController {

    @Resource
    IYaoService yaoService;

    @Resource
    IStackPlayerService playerService;

    @Resource
    IGameOrderService orderService;

    @Resource
    HuiYuanService huiYuanService;


    @Value("${alipay.alipayCertPath}")
    private String alipayCertPublicKey;

    @ApiOperation("登陆/获取token")
    @PostMapping("/login")
    public Rv<YaoEntityVO> platLogin(@RequestBody LoginDto dto) {
        return new Rv<>(yaoService.login(dto));
    }

    @ApiOperation("玩家登录")
    @PostMapping("/playerLogin")
    public Rv<StackPlayerEntityVO> playerLogin(@RequestBody LoginDto dto) {
        return new Rv<>(playerService.playerLogin(dto));
    }

    @ApiOperation("玩家注册")
    @PostMapping("/playerSignin")
    public Rv<StackPlayerEntityVO> playerSignin(@RequestBody LoginDto dto) {
        return new Rv<>(playerService.saveStackPlayerEntity(dto));
    }

//    @ApiOperation("订单查询")
//    @PostMapping("/queryAlipayOrder")
//    public Rv<AlipayResposeVO> queryAlipayOrder(@RequestBody PayByAliPay vo) {
//        return new Rv<>(alipayService.queryPay(vo));
//    }
//
//
//    @ApiOperation("订单退款")
//    @PostMapping("/refundAlipayOrder")
//    public Rv<Integer> refundAlipayOrder(@RequestBody PayByAliPay vo) {
//        return new Rv<>(alipayService.refundPay(vo) ? 1 : 0);
//    }

    @ApiOperation("test")
    @GetMapping("/test")
    public Rv<String> test() {
//        huiYuanService.setBillStatus("C250916238577613", 1, "成功处理");
//        huiYuanService.getDetailOrder("C250916238577613");
//        huiYuanService.getHuiYuanOrders();

//        String account = "wwbbinB6bGLIy1";
//        String orderNo = account.substring(account.length() - 8);
//        String paylerLoginName = account.substring(0, account.length() - 8);
//        log.info("ORG: {}  orderNo: {}  name: {}", account, orderNo, paylerLoginName);
        return new Rv<>("OK");
    }

//    @ApiOperation("添加订单")
//    @PostMapping("/pay")
//    public Rv<DrawerPollDTO> createOrderByPay(@RequestBody PayByAliPay vo) {
//        return new Rv<>(alipayService.savePay(vo));
//    }

    @ApiOperation("HuiYUAN回调")
    @PostMapping("/callbackForJw")
    public String createPcOrderByPay(@RequestParam Map<String,String> params) {
        log.info("HuiYUAN回调");
        params.forEach((key,value)->{
            log.info("KEY: {} - VALUE: {}",key,value);
        });

        BillTable bill = new BillTable();
        bill.setBillNo(params.get("bill_no"));
        bill.setProductName(params.get("product_code"));
        bill.setProductCode(params.get("product_code"));
        bill.setParPrice(params.get("par_price"));
        bill.setBillStatus(params.get("bill_status"));
        bill.setChargeAccount(params.get("charge_account"));
        huiYuanService.handleBill(bill);
        return "OK";
    }

    @ApiOperation("获取随机玩家用户名")
    @GetMapping("/getPlayer")
    public Rv<PlayerRes> getPlayer() {
        StackPlayerEntity player = playerService.findPlayerForPay();
        PlayerRes res = new PlayerRes();

        String orderNo = "";
        boolean repeti = true;
        while (repeti) {
            orderNo = JuliaUtils.generateRandomForOrderNo();
            GameOrderEntity gameOrder = orderService.findOneByOrderNo(orderNo);
            if (ObjectUtils.isEmpty(gameOrder)) {
                repeti = false;
            }
        }
        orderService.initOrder(orderNo,player.getLoginName());
        res.setPlayerName(player.getLoginName());
        res.setOrderNo(orderNo);
        return new Rv<>(res);
    }
}
