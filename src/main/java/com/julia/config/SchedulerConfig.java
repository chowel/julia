package com.julia.config;


import com.julia.tool.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * @program: marina
 * @description: 定时任务
 * @author: Chowel.Master
 * @create: 2024-12-31 15:52
 **/
@Slf4j
@Configuration
public class SchedulerConfig implements SchedulingConfigurer {
    @Resource
    RedisUtils redisUtils;
    // 创建一个定制的线程池
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
        executor.setPoolSize(10);
        executor.setThreadNamePrefix("task-thread");
        //设置饱和策略
        //CallerRunsPolicy：线程池的饱和策略之一，当线程池使用饱和后，直接使用调用者所在的线程来执行任务；如果执行程序已关闭，则会丢弃该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskScheduler());
        taskRegistrar.addTriggerTask(
                // 定义执行任务内容
                () -> {
//                    log.info("reddis 保活定时");
                    String alive= (String) redisUtils.get("KL:alive:");
                    if(StringUtils.hasLength(alive)){
                        redisUtils.set("KL:alive:","1",300);
                    }
                },
                // 定义执行周期
                triggerContext -> new CronTrigger("30 * * * * ?").nextExecutionTime(triggerContext)
        );


    }

}
