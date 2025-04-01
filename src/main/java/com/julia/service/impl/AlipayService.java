package com.julia.service.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.diagnosis.DiagnosisUtils;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.domain.GoodsDetail;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.julia.entity.GameOrderEntity;
import com.julia.entity.StackPlayerEntity;
import com.julia.entity.alipaymodel.AlipayResposeVO;
import com.julia.entity.alipaymodel.PayByAliPay;
import com.julia.model.alipay.AliPayCreate;
import com.julia.model.dto.DrawerPollDTO;
import com.julia.model.vo.GameOrderEntityVO;
import com.julia.service.IGameOrderService;
import com.julia.service.IStackPlayerService;
import com.julia.tool.JuliaException;
import com.julia.tool.JuliaUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

    @Value("${sign.salt}")
    private String payKey;


    @Value("${alipay.notifyUrl}")
    private String notifyUrl;


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

        AlipayTradeWapPayResponse response = alipayClient.pageExecute(request, "POST");
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

    /**
     * @Description: 查询接口
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    @SneakyThrows
    public AlipayResposeVO queryPay(PayByAliPay dto) {

        if (!payKey.equals(dto.getPayKey())) {
            throw new JuliaException("非法参数");
        }
        // 构造请求参数以调用接口
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();

        model.setOutTradeNo(dto.getOrderNo());

        request.setBizModel(model);

        AlipayTradeQueryResponse response = alipayClient.certificateExecute(request);

        if (response.isSuccess()) {
//            log.info("query trade_status : {}", response.getTradeStatus());
//            log.info("query total_amount : {}", response.getTotalAmount());
            AlipayResposeVO resposeVO = new AlipayResposeVO();
            resposeVO.setOrderNo(response.getOutTradeNo());
            resposeVO.setTotalAmount(response.getTotalAmount());
            resposeVO.setTradeStats(response.getTradeStatus());
            return resposeVO;
        } else {
            // sdk版本是"4.38.0.ALL"及以上,可以参考下面的示例获取诊断链接
            String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(response);
            log.error(diagnosisUrl);
        }
        return null;
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

        if (!payKey.equals(dto.getPayKey())) {
            throw new JuliaException("非法参数");
        }
        GameOrderEntity order = orderService.getOne(new QueryWrapper<GameOrderEntity>().eq("order_no", dto.getOrderNo()));
        if (ObjectUtils.isEmpty(order)) {
            throw new JuliaException("非法参数");
        }
        // 构造请求参数以调用接口
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();


        model.setOutTradeNo(dto.getOrderNo());

        Long price = order.getTotal();
        // 设置退款金额
        model.setRefundAmount(String.format("%.2f", price / 100.0));
//        request.setNotifyUrl(notifyUrl);
        request.setBizModel(model);

        AlipayTradeRefundResponse response = alipayClient.certificateExecute(request);

        if (response.isSuccess()) {
            log.info("调用退款成功");
            if ("Y".equals(response.getFundChange())) {
                order.setStatus(4);
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

    public DrawerPollDTO savePay(PayByAliPay pay) {
        log.info("KEY: {}", pay.getPayKey());
        if (payKey.equals(pay.getPayKey())) {
            GameOrderEntity order = new GameOrderEntity();
            StackPlayerEntity player = playerService.findPlayerForPay();
            order.setOrderNo(JuliaUtils.GeneratorOderNo(Math.toIntExact(player.getUserId())));
            order.setPlayerId(Math.toIntExact(player.getUserId()));
            order.setSubject(pay.getSubject());
            order.setTotal(pay.getPrice());
            order.setPlayerName(player.getNickName());
            if (orderService.save(order)) {
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
                    return dto;
                }
            }
        }
        return null;
    }

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
            playerService.addCoin(Long.valueOf(order.getPlayerId()), Double.parseDouble(params.get("total_amount")));
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

}

