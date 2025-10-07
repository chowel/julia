package com.julia.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Tomcat[chowel]
 * @Description:线程池
 * @date 2022/3/7 4:00
 */
@Slf4j
@Configuration
@EnableAsync
public class ThreadPoolConfig {

    /**
     * 线程池
     * @return ThreadPoolTaskExecutor
     */
    @Bean("handleNotificationExecutor")
    public ThreadPoolTaskExecutor handleNotificationThreadPool() {
        RejectedExecutionHandler handler = new MyIgnorePolicy();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(20);
        // 设置最大线程数
        executor.setMaxPoolSize(30);
        // 设置队列容量
        executor.setQueueCapacity(10);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(5);
        // 设置默认线程名称
        executor.setThreadNamePrefix("Notification-ThreadPool-");
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(handler);
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }

    /**
     * 邀请线程池
     * @return ThreadPoolTaskExecutor
     */
    @Bean("handleInviteExecutor")
    public ThreadPoolTaskExecutor handleInviteThreadPool() {
        RejectedExecutionHandler handler = new MyIgnorePolicy();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(15);
        // 设置最大线程数
        executor.setMaxPoolSize(30);
        // 设置队列容量
        executor.setQueueCapacity(15);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(0);
        // 设置默认线程名称
        executor.setThreadNamePrefix("flow-InviteThreadPool-");
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(handler);
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }

    public static class MyIgnorePolicy implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            doLog(r, e);
        }

        private void doLog(Runnable r, ThreadPoolExecutor e) {
            // 可做日志记录等
            log.info("{} rejected---文件被抛弃",  r.toString());
            log.info("completedTaskCount: {}", e.getCompletedTaskCount());
        }
    }

}
