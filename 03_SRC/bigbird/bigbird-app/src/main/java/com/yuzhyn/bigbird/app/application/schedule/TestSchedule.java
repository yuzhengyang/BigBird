package com.yuzhyn.bigbird.app.application.schedule;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.*;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;
import pers.yuzhyn.azylee.core.threads.sleeps.Sleep;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Component
@EnableScheduling
@EnableAsync
public class TestSchedule  {

    @Async
    @Scheduled(cron = "0/1 * * * * *")
    public void show() {
        Thread t = Thread.currentThread();
        Sleep.s(2);
        System.out.println("TestSchedule" + new Date() + " : Annotation：is show run :" + t.getId() + "," + t.getName());
    }

//    @Async
    @Scheduled(cron = "0/5 * * * * *")
    public void show2() {
        Thread t = Thread.currentThread();
        Sleep.s(5);
        System.out.println("TestSchedule" + new Date() + " : 5秒间隔 Scheduled :" + t.getId() + "," + t.getName());
    }

//    @Override
//    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
//        taskRegistrar.setScheduler(taskExecutor());
//    }
//
//    @Bean(destroyMethod = "shutdown")
//    public Executor taskExecutor() {
//        return Executors.newScheduledThreadPool(100);
//    }
}
