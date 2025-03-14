package com.julia.config;


import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @ClassName AliPayConfig
 * @Description 阿里支付配置
 * @Author 陈贤一
 * @Date 2023/3/21 15:17
 * @Version 1.0
 **/
@Component
public class AliPayConfig {

    @Value("${alipay.url}")
    private String URL;


    @Value("${alipay.appId}")
    private String APPID;

    @Value("${alipay.privateKey}")
    private String PRIVATE_KEY;

    @Value("${alipay.appCertPath}")
    private String app_cert_path;

    @Value("${alipay.alipayRootCertPath}")
    private String alipay_root_cert_path;

    @Value("${alipay.alipayCertPath}")
    private String alipay_cert_path;

    @Value("${alipay.notifyUrl}")
    private String notifyUrl;

    @Bean
    public AlipayClient alipayClient() throws AlipayApiException {
//        System.out.println("alipay_root_cert_path ->->:"+alipay_root_cert_path);
        AlipayConfig alipayConfig = new AlipayConfig();
        //设置网关地址
        alipayConfig.setServerUrl(URL);
        //设置应用ID
        alipayConfig.setAppId(APPID);
        //设置应用私钥
        alipayConfig.setPrivateKey(PRIVATE_KEY);
        //设置请求格式，固定值json
        alipayConfig.setFormat("JSON");
        //设置字符集
        alipayConfig.setCharset("UTF-8");
        //设置签名类型
        alipayConfig.setSignType("RSA2");
        //设置应用公钥证书路径
        alipayConfig.setAppCertPath(app_cert_path);
        //设置支付宝公钥证书路径
        alipayConfig.setAlipayPublicCertPath(alipay_cert_path);
        //设置支付宝根证书路径
        alipayConfig.setRootCertPath(alipay_root_cert_path);
        //构造client
        return new DefaultAlipayClient(alipayConfig);
        //构造API请求
//        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
//发送请求
//        AlipayTradeQueryResponse response = alipayClient.certificateExecute(request);
    }

}
