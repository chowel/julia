package com.julia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

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
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        RestTemplate restTemplate = new RestTemplate(factory);
        // 使用 XML 转换器
        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
        converters.removeIf(c -> c instanceof MappingJackson2XmlHttpMessageConverter);
        converters.add(new MappingJackson2XmlHttpMessageConverter());

        return restTemplate;
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(15000);
        factory.setReadTimeout(5000);
        return factory;
    }

}
