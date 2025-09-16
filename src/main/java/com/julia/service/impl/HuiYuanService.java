package com.julia.service.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.diagnosis.DiagnosisUtils;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
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
import com.julia.entity.PayConfigEntity;
import com.julia.entity.StackPlayerEntity;
import com.julia.entity.YaoEntity;
import com.julia.entity.alipaymodel.AlipayResposeVO;
import com.julia.entity.alipaymodel.PayByAliPay;
import com.julia.model.alipay.AliPayCreate;
import com.julia.model.dto.DrawerPollDTO;
import com.julia.service.*;
import com.julia.tool.JuliaException;
import com.julia.tool.JuliaUtils;
import com.julia.tool.RedisUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
public class HuiYuanService {


    @Resource
    private IStackPlayerService playerService;

    @Resource
    private IGameOrderService orderService;

    @Resource
    private IYaoService yaoService;

    @Resource
    private IPayConfigService payConfigService;

    @Resource
    private IRocketService rocketService;

    @Resource
    private RedisUtils redisUtils;

    @Value("${huiyuan.agentId}")
    private String agentId;

    @Value("${huiyuan.dealUser}")
    private String dealUser;

    @Value("${huiyuan.md5Key}")
    private String md5Key;

    @Value("${huiyuan.domain}")
    private String domain;

    @Resource
    private RestTemplate restTemplate;


    public void getHuiYuanOrders() {
        String sign = genSign();
        String url = domain + "agent_id=" + agentId + "&down_time=" + getDownTime() + "&deal_urser=" + dealUser + "&sign=" + sign;

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_XML));
        HttpEntity<String> requestEntity = new HttpEntity<>( headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        String xmlString = response.getBody();
        System.out.println(xmlString);

    }


    private String getDownTime() {
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 定义格式：4位年+2位月+2位日+2位时+2位分+2位秒
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        // 格式化为字符串
        return now.format(formatter);

    }

    // md5加密
    private String genMd5(String str) {
        StringBuilder result = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(str.getBytes(StandardCharsets.UTF_8));
            for (byte b : digest) {
                result.append(String.format("%02x", b & 0xff));
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return result.toString();
    }


    // 生成签名
    private String genSign() {
        // 拼接字符串
        String data = "agent_id=" + agentId + "&down_time=" + getDownTime() + "&deal_user=" + dealUser + "|||" + md5Key;
        // 生成签名
        return genMd5(data).toLowerCase();
    }

}

