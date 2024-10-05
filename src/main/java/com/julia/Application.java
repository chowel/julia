package com.julia;

import cn.dev33.satoken.secure.BCrypt;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2023-10-30 14:46
 **/
@EnableAsync
@EnableScheduling
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
//        System.out.println(BCrypt.hashpw("xixixixi"));
        SpringApplication.run(Application.class);
    }
}
