package com.julia.config;

import com.julia.entity.CoinLogEntity;
import com.julia.enums.RedisKeyEnum;
import com.julia.service.ICoinLogService;
import com.julia.tool.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;


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

    @Resource
    ICoinLogService coinLogService;

    private LocalTime now = LocalTime.now();

    // 创建一个定制的线程池
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
        executor.setPoolSize(10);
        executor.setThreadNamePrefix("Scheduler-Thread-");
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
// 每日统计
                    if (now.getHour() == 18 && now.getMinute() == 0) {
                        int count = 0;
                        if(redisUtils.hasKey(RedisKeyEnum.VISITDAILY.getKey())){
                            count = (int)redisUtils.get(RedisKeyEnum.VISITDAILY.getKey());
                        }

                        // 获取昨天的日期
                        LocalDate yesterday = LocalDate.now().minusDays(1);
                        // 定义格式
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        // 转为字符串
                        String yesterdayStr = yesterday.format(formatter);

                        CoinLogEntity coinLog = new CoinLogEntity();
                        coinLog.setCoin(count);
                        coinLog.setCoinDate(yesterdayStr);
                        coinLogService.save(coinLog);
                    }
//                    log.info("每30秒执行一次 redis 保活");
                    if (!redisUtils.hasKey(RedisKeyEnum.ALIVE.getKey())) {
                        redisUtils.set(RedisKeyEnum.ALIVE.getKey(), 1, 300);
                    }
                },
                // 定义执行周期
                triggerContext -> new CronTrigger("0,30 * * * * ?").nextExecutionTime(triggerContext)
        );


    }

}
