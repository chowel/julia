package com.julia.service.impl;

import com.alipay.api.AlipayClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @program: julia
 * @description: 支付宝支付
 * @author: Chowel.Master
 * @create: 2025-03-12 15:21
 **/

@Service
public class AlipayService {

    @Resource
    private AlipayClient alipayClient;
}
