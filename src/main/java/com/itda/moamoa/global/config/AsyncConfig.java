package com.itda.moamoa.global.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {
    @Override
    @Bean(name = "mailExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);   //스레드 풀의 최소 스레드 개수, 초기 2개 대기
        executor.setMaxPoolSize(5);    //스레드 풀의 최대 크기
        executor.setQueueCapacity(10); //작업 큐의 최대 용량, 스레드가 모두 실행 중일 때 10개의 작업만 대기 가능
        executor.setThreadNamePrefix("Async MailExecutor-"); //스레드의 접두사 이름
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return AsyncConfigurer.super.getAsyncUncaughtExceptionHandler();
    }
}
