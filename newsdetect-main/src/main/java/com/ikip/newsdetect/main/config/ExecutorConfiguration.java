package com.ikip.newsdetect.main.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@EnableScheduling
public class ExecutorConfiguration {

    @Bean
    @Primary
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(1);
        taskExecutor.setCorePoolSize(1);
        taskExecutor.setThreadNamePrefix("ILSA-Async-");
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Bean
    public TaskScheduler getSchedulerExecutor() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(1);
        taskScheduler.setThreadNamePrefix("ILSA-Scheduler-");
        taskScheduler.initialize();
        return taskScheduler;
    }
/* TODO
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }
  */
}
