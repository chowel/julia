package com.julia.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2025-09-17 18:46
 **/
@Component
public class XmlApiHelper {

    private final RestTemplate restTemplate;
    private final XmlMapper xmlMapper;

    public XmlApiHelper() {
        this.restTemplate = new RestTemplate();
        this.xmlMapper = new XmlMapper();
        this.xmlMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        this.xmlMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public <T> T postForXml(String url, Object request, Class<T> responseType) throws Exception {
        // 1. 构建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        HttpEntity<?> requestEntity = new HttpEntity<>(request, headers);
        // 2. 发送请求，接收字节流（关键：避免 String 提前解码乱码）
        ResponseEntity<byte[]> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                byte[].class
        );

        byte[] rawBytes = response.getBody();
        if (rawBytes == null || rawBytes.length == 0) {
            throw new RuntimeException("响应体为空");
        }
        // 3. 使用 GBK 解码（兼容 gb2312，支持更多汉字）
        String xmlContent = new String(rawBytes, Charset.forName("GBK"));

        // 4. 解析 XML
        return xmlMapper.readValue(xmlContent, responseType);
    }

    public <T> T getForXml(String url, Class<T> responseType) throws Exception {
        // 1. 构建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        // 2. 发送请求，接收字节流（关键：避免 String 提前解码乱码）
        ResponseEntity<byte[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                byte[].class
        );

        byte[] rawBytes = response.getBody();
        if (rawBytes == null || rawBytes.length == 0) {
            throw new RuntimeException("响应体为空");
        }
        // 3. 使用 GBK 解码（兼容 gb2312，支持更多汉字）
        String xmlContent = new String(rawBytes, Charset.forName("GBK"));

        // 4. 解析 XML
        return xmlMapper.readValue(xmlContent, responseType);
    }
}
