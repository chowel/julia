package com.julia.service.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.diagnosis.DiagnosisUtils;
import com.alipay.api.domain.*;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.julia.entity.GameOrderEntity;
import com.julia.entity.StackPlayerEntity;
import com.julia.entity.YaoEntity;
import com.julia.entity.alipaymodel.AlipayResposeVO;
import com.julia.entity.alipaymodel.PayByAliPay;
import com.julia.enums.RedisKeyEnum;
import com.julia.model.alipay.AliPayCreate;
import com.julia.model.dto.DrawerPollDTO;
import com.julia.model.vo.GameOrderEntityVO;
import com.julia.service.IGameOrderService;
import com.julia.service.IRocketService;
import com.julia.service.IStackPlayerService;
import com.julia.service.IYaoService;
import com.julia.tool.JuliaException;
import com.julia.tool.JuliaUtils;
import com.julia.tool.RedisUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: julia
 * @description: 支付宝支付
 * @author: Chowel.Master
 * @create: 2025-03-12 15:21
 **/

@Service
@Slf4j
public class AlipayService {

    @Resource
    private AlipayClient alipayClient;

    @Resource
    private IStackPlayerService playerService;

    @Resource
    private IGameOrderService orderService;

    @Resource
    private IYaoService yaoService;

    @Resource
    private IRocketService rocketService;

    @Resource
    private RedisUtils redisUtils;


    @Value("${alipay.notifyUrl}")
    private String notifyUrl;


    @Value("${alipay.appId}")
    private String appID;

    /**
     * @Description: 创建支付接口
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    @SneakyThrows
    public String createPay(AliPayCreate aliPayCreate) {
        // 构造请求参数以调用接口
        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        // 设置商户订单号
        model.setOutTradeNo(aliPayCreate.getOutTradeNo());
        // 设置订单总金额
        model.setTotalAmount(aliPayCreate.getTotalAmount());
        // 设置订单标题
        model.setSubject(aliPayCreate.getSubject());
        // 设置产品码
        model.setProductCode("QUICK_WAP_WAY");
        if (StringUtils.hasLength(aliPayCreate.getAuthToken())) {
            // 设置针对用户授权接口
            model.setAuthToken(aliPayCreate.getAuthToken());
        }
        if (StringUtils.hasLength(aliPayCreate.getQuitUrl())) {
            // 设置用户付款中途退出返回商户网站的地址
            model.setQuitUrl(aliPayCreate.getQuitUrl());
        }
        if (!ObjectUtils.isEmpty(aliPayCreate.getGoods()) && aliPayCreate.getGoods().size() > 1) {
            model.setGoodsDetail(aliPayCreate.getGoods());
        }
        if (StringUtils.hasLength(aliPayCreate.getTimeExpire())) {
            // 设置订单绝对超时时间
            model.setTimeExpire(aliPayCreate.getTimeExpire());
        }
        if (StringUtils.hasLength(aliPayCreate.getBusinessParams())) {
            model.setBusinessParams(aliPayCreate.getBusinessParams());
        }
        if (StringUtils.hasLength(aliPayCreate.getPassbackParams())) {
            model.setPassbackParams(aliPayCreate.getPassbackParams());
        }
        if (StringUtils.hasLength(aliPayCreate.getMerchantOrderNo())) {
            // 设置商户的原始订单号
            model.setMerchantOrderNo(aliPayCreate.getMerchantOrderNo());
        }
        request.setNotifyUrl(notifyUrl);
        request.setBizModel(model);
        AlipayTradeWapPayResponse response;
        if (aliPayCreate.getMethod() == 1) {
            response = alipayClient.pageExecute(request, "POST");
        } else {
            response = alipayClient.pageExecute(request, "GET");
        }

        if (response.isSuccess()) {
            log.info("调用成功");
            return response.getBody();
        } else {
            // sdk版本是"4.38.0.ALL"及以上,可以参考下面的示例获取诊断链接
            String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(response);
            log.error(diagnosisUrl);
        }
        return null;
    }


    @SneakyThrows
    public String createPayByPc(AliPayCreate aliPayCreate){
        // 构造请求参数以调用接口
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        // 设置商户订单号
        model.setOutTradeNo(aliPayCreate.getOutTradeNo());
        // 设置订单总金额
        model.setTotalAmount(aliPayCreate.getTotalAmount());
        // 设置订单标题
        model.setSubject(aliPayCreate.getSubject());
        // 设置产品码
        model.setProductCode("FAST_INSTANT_TRADE_PAY");
        // 设置PC扫码支付的方式
        model.setQrPayMode("2");
        request.setNotifyUrl(notifyUrl);
        request.setBizModel(model);
        // 第三方代调用模式下请设置app_auth_token
        // request.putOtherTextParam("app_auth_token", "<-- 请填写应用授权令牌 -->");

        AlipayTradePagePayResponse response = alipayClient.pageExecute(request, "GET");
        // 如果需要返回GET请求，请使用
        // AlipayTradePagePayResponse response = alipayClient.pageExecute(request, "GET");
        String pageRedirectionData = response.getBody();
//        System.out.println(pageRedirectionData);

        if (response.isSuccess()) {
            log.info("pc调用成功");
            return response.getBody();
        } else {
            log.error("调用失败");
            // sdk版本是"4.38.0.ALL"及以上,可以参考下面的示例获取诊断链接
             String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(response);
             log.info(diagnosisUrl);
        }
        return null;
    }
    /**
     * @Description: 查询接口
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    @SneakyThrows
    public AlipayResposeVO queryPay(PayByAliPay dto) {

        GameOrderEntity order = orderService.getOne(new QueryWrapper<GameOrderEntity>()
                .eq(StringUtils.hasLength(dto.getOrderNo()),"order_no", dto.getOrderNo())
                .eq(StringUtils.hasLength(dto.getPayKey()),"merchant_no",dto.getPayKey())
                .eq(StringUtils.hasLength(dto.getOutOrderNo()),"out_order_no",dto.getOutOrderNo())
        );

        if(ObjectUtils.isEmpty(order)){
            throw new JuliaException("订单不存在");
        }
//        YaoEntity jh = yaoService.getCallBackOrKey("jiahe");
//        if (!jh.getAvatar().equals(dto.getPayKey())) {
//            throw new JuliaException("非法参数");
//        }
        // 构造请求参数以调用接口
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();

        model.setOutTradeNo(order.getOrderNo());

        request.setBizModel(model);

        AlipayTradeQueryResponse response = alipayClient.certificateExecute(request);
        AlipayResposeVO resposeVO = new AlipayResposeVO();
        resposeVO.setOrderNo(order.getOrderNo());
        resposeVO.setOutOrderNo(order.getOutOrderNo());
        Long price = order.getTotal();
        resposeVO.setTotalAmount(String.format("%.2f", price / 100.0));

        if (response.isSuccess()) {
            if(!response.getTradeStatus().equals(order.getTradeStatus())){
                order.setTradeStatus(response.getTradeStatus());
                orderService.save(order);
            }
            resposeVO.setTradeStatus(response.getTradeStatus());
        } else {
            // sdk版本是"4.38.0.ALL"及以上,可以参考下面的示例获取诊断链接
            String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(response);
            log.error(diagnosisUrl);
            resposeVO.setTradeStatus(order.getTradeStatus());
        }
        return resposeVO;
    }

    /**
     * @Description: 退款接口
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    @SneakyThrows
    public Boolean refundPay(PayByAliPay dto) {
        GameOrderEntity order = orderService.getOne(new QueryWrapper<GameOrderEntity>()
                .eq(StringUtils.hasLength(dto.getOrderNo()),"order_no", dto.getOrderNo())
                .eq(StringUtils.hasLength(dto.getOutOrderNo()),"out_order_no",dto.getOutOrderNo())
        );

        if(ObjectUtils.isEmpty(order)){
            throw new JuliaException("订单不存在");
        }
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        model.setOutTradeNo(order.getOrderNo());
        Long price = order.getTotal();
        model.setRefundAmount(String.format("%.2f", price / 100.0));
        request.setBizModel(model);
        AlipayTradeRefundResponse response = alipayClient.certificateExecute(request);
        if (response.isSuccess()) {
            log.info("调用退款成功");
            if ("Y".equals(response.getFundChange())) {
                order.setStatus(4);
                order.setTradeStatus("TRADE_CLOSED");
                orderService.updateById(order);
                return true;
            }
        } else {
            // sdk版本是"4.38.0.ALL"及以上,可以参考下面的示例获取诊断链接
            String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(response);
            log.error(diagnosisUrl);
        }
        return false;
    }

    /**
     * @Description: 非游戏创建订单
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    public DrawerPollDTO savePay(PayByAliPay pay) {
        YaoEntity jh = yaoService.getCallBackOrKey("jiahe");
        if (jh.getAvatar().equals(pay.getPayKey())) {
            GameOrderEntity order = new GameOrderEntity();
            StackPlayerEntity player = playerService.findPlayerForPay();
            order.setOrderNo(JuliaUtils.GeneratorOderNo(Math.toIntExact(player.getUserId())));
            order.setPlayerId(Math.toIntExact(player.getUserId()));
            order.setSubject(pay.getSubject());
            order.setTotal(pay.getPrice());
            order.setPlayerName(player.getNickName());
            order.setStatus(6);
            order.setUrl(pay.getNoticeURL());
            order.setOutOrderNo(pay.getOutOrderNo());
            order.setMerchantNo(pay.getPayKey());
            if (orderService.save(order)) {
//                redisUtils.set(RedisKeyEnum.WAITORDER.getKey() + order.getOrderNo(), order);
                AliPayCreate aliPayCreate = new AliPayCreate();
                aliPayCreate.setOutTradeNo(order.getOrderNo());
                aliPayCreate.setSubject(order.getSubject());
                Long price = order.getTotal();
                aliPayCreate.setTotalAmount(String.format("%.2f", price / 100.0));
                aliPayCreate.setMethod(2);
                String payUrl = createPay(aliPayCreate);
                if (StringUtils.hasLength(payUrl)) {
                    DrawerPollDTO dto = new DrawerPollDTO();
                    dto.setOrderNo(order.getOrderNo());
                    dto.setPayUrl(payUrl);
                    dto.setPrice(String.format("%.2f", price / 100.0));
                    dto.setSubject(order.getSubject());
                    dto.setOutOrderNo(order.getOutOrderNo());
                    return dto;
                }
            }
        }
        return null;
    }

    /**
     * @Description: 非游戏创建订单-PC
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    public DrawerPollDTO savePayByPc(PayByAliPay pay){
        YaoEntity jh = yaoService.getCallBackOrKey("jiahe");
        if (jh.getAvatar().equals(pay.getPayKey())) {
            GameOrderEntity order = new GameOrderEntity();
            StackPlayerEntity player = playerService.findPlayerForPay();
            order.setOrderNo(JuliaUtils.GeneratorOderNo(Math.toIntExact(player.getUserId())));
            order.setPlayerId(Math.toIntExact(player.getUserId()));
            order.setSubject(pay.getSubject());
            order.setTotal(pay.getPrice());
            order.setPlayerName(player.getNickName());
            order.setStatus(6);
            order.setUrl(pay.getNoticeURL());
            order.setOutOrderNo(pay.getOutOrderNo());
            order.setMerchantNo(pay.getPayKey());
            if (orderService.save(order)) {
//                redisUtils.set(RedisKeyEnum.WAITORDER.getKey() + order.getOrderNo(), order);
                AliPayCreate aliPayCreate = new AliPayCreate();
                aliPayCreate.setOutTradeNo(order.getOrderNo());
                aliPayCreate.setSubject(order.getSubject());
                Long price = order.getTotal();
                aliPayCreate.setTotalAmount(String.format("%.2f", price / 100.0));
                aliPayCreate.setMethod(2);
                String payUrl = createPayByPc(aliPayCreate);
                if (StringUtils.hasLength(payUrl)) {
                    DrawerPollDTO dto = new DrawerPollDTO();
                    dto.setOrderNo(order.getOrderNo());
                    dto.setPayUrl(payUrl);
                    dto.setPrice(String.format("%.2f", price / 100.0));
                    dto.setSubject(order.getSubject());
                    dto.setOutOrderNo(order.getOutOrderNo());
                    return dto;
                }
            }
        }
        return null;
    }
    /**
     * @Description: 处理回调
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    public void handleCallBack(Map<String, String> params) {
        GameOrderEntity order = orderService.getOne(new QueryWrapper<GameOrderEntity>().eq("order_no", params.get("out_trade_no")));
        if (!ObjectUtils.isEmpty(order)) {
            if (StringUtils.hasLength(params.get("buyer_open_id"))) {
                order.setBuyerId(params.get("buyer_open_id"));
            }

            if (StringUtils.hasLength(params.get("buyer_logon_id"))) {
                order.setBuyerLogonId(params.get("buyer_logon_id"));
            }

            if (StringUtils.hasLength(params.get("trade_status"))) {
                order.setTradeStatus(params.get("trade_status"));
                if ("TRADE_SUCCESS".equals(params.get("trade_status"))) {
                    order.setStatus(1);
                }
            }

            if (StringUtils.hasLength(params.get("trade_no"))) {
                order.setTradeNo(params.get("trade_no"));
            }

            if (StringUtils.hasLength(params.get("total_amount"))) {
                order.setTotalAmount((long) (Double.parseDouble(params.get("total_amount")) * 100));
            }

            if (StringUtils.hasLength(params.get("receipt_amount"))) {
                order.setReceiptAmount((long) (Double.parseDouble(params.get("receipt_amount")) * 100));
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            if (StringUtils.hasLength(params.get("gmt_create"))) {
                order.setGmtCreate(LocalDateTime.parse(params.get("gmt_create"), formatter));
            }

            if (StringUtils.hasLength(params.get("gmt_payment"))) {
                order.setGmtPayment(LocalDateTime.parse(params.get("gmt_payment"), formatter));
            }

            if (StringUtils.hasLength(params.get("gmt_close"))) {
                order.setGmtClose(LocalDateTime.parse(params.get("gmt_close"), formatter));
            }

            DateTimeFormatter formatterSS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
            if (StringUtils.hasLength(params.get("gmt_refund"))) {
                order.setGmtRefund(LocalDateTime.parse(params.get("gmt_refund"), formatterSS));
            }

            orderService.updateById(order);
            if (JuliaUtils.startsWithHttpOrHttps(order.getUrl())) {
                YaoEntity jh = yaoService.getCallBackOrKey("jiahe");
                Long price = order.getTotal();
                Map<String, Object> callBackParams = new HashMap<>(5);
                String sign = order.getOrderNo() + jh.getAvatar();
                callBackParams.put("orderNo", order.getOrderNo());
                callBackParams.put("outOrderNo", order.getOutOrderNo());
                callBackParams.put("totalAmount", String.format("%.2f", price / 100.0));
                callBackParams.put("tradeStatus", order.getTradeStatus());
                callBackParams.put("payTime", String.valueOf(order.getGmtPayment()));
                callBackParams.put("sign", DigestUtils.md5DigestAsHex(sign.getBytes(StandardCharsets.UTF_8)));
                callBackParams.put("userOpenId",order.getBuyerId());
                callBackParams.put("userLoginId",order.getBuyerLogonId());
                callBackParams.put("appId",appID);
                rocketService.handleCallBack(order.getUrl(), callBackParams);
            }
        }
    }


    public DrawerPollDTO savePayFormGame(String orderNo) {
        GameOrderEntity order = orderService.findOneByOrderNo(orderNo);
        AliPayCreate aliPayCreate = new AliPayCreate();
        aliPayCreate.setOutTradeNo(order.getOrderNo());
        aliPayCreate.setSubject(order.getSubject());
        Long price = order.getTotal();

        aliPayCreate.setTotalAmount(String.format("%.2f", price / 100.0));
        String payUrl = createPay(aliPayCreate);
        if (StringUtils.hasLength(payUrl)) {
            DrawerPollDTO dto = new DrawerPollDTO();
            dto.setOrderNo(order.getOrderNo());
            dto.setPayUrl(payUrl);
            dto.setPrice(String.format("%.2f", price / 100.0));
            dto.setSubject(order.getSubject());
            return dto;
        }
        return null;
    }

    public boolean reqCallBack(PayByAliPay vo){
        GameOrderEntity order = orderService.getOne(new QueryWrapper<GameOrderEntity>()
                .eq(StringUtils.hasLength(vo.getOrderNo()),"order_no", vo.getOrderNo())
                .eq(StringUtils.hasLength(vo.getPayKey()),"merchant_no",vo.getPayKey())
                .eq(StringUtils.hasLength(vo.getOutOrderNo()),"out_order_no",vo.getOutOrderNo())
        );

        if(ObjectUtils.isEmpty(order)){
            throw new JuliaException("订单不存在");
        }
        if (StringUtils.hasLength(order.getUrl())) {
            YaoEntity jh = yaoService.getCallBackOrKey("jiahe");
            Long price = order.getTotal();
            Map<String, Object> callBackParams = new HashMap<>(5);
            String sign = order.getOrderNo() + jh.getAvatar();
            callBackParams.put("orderNo", order.getOrderNo());
            callBackParams.put("outOrderNo", order.getOutOrderNo());
            callBackParams.put("totalAmount", String.format("%.2f", price / 100.0));
            callBackParams.put("tradeStatus", order.getTradeStatus());
            callBackParams.put("payTime", String.valueOf(order.getGmtPayment()));
            callBackParams.put("sign", DigestUtils.md5DigestAsHex(sign.getBytes(StandardCharsets.UTF_8)));
            rocketService.handleCallBack(order.getUrl(), callBackParams);
        }
        return true;
    }

}

