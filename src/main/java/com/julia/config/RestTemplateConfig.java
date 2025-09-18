package com.julia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName RestTemplateConfig
 * @Description RestTemplateConfig
 * @Author chowel
 * @Date 2022/10/13 15:02
 * @Version 1.0
 **/
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // 获取默认的转换器列表
        List<HttpMessageConverter<?>> converters = new ArrayList<>();

        // 1. 添加 JSON 转换器
        converters.add(new MappingJackson2HttpMessageConverter());

        // 2. 添加 XML 转换器
        converters.add(new MappingJackson2XmlHttpMessageConverter());

        // 3. 可选：添加 String 转换器（防止 text/plain 无法处理）
        converters.add(new StringHttpMessageConverter());

        // 设置转换器
        restTemplate.setMessageConverters(converters);

        return restTemplate;
    }
}
