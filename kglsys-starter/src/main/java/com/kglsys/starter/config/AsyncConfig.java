package com.kglsys.starter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync // 启用 Spring 的异步方法执行能力
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);   // 核心线程数
        executor.setMaxPoolSize(10);   // 最大线程数
        executor.setQueueCapacity(25); // 队列容量
        executor.setThreadNamePrefix("JudgeThread-");
        executor.initialize();
        return executor;
    }
}