package com.yuzhyn.bigbird.app.application.customization.schedule;

import com.yuzhyn.bigbird.app.application.internal.entity.SysStatusLog;
import com.yuzhyn.bigbird.app.application.internal.mapper.SysStatusLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.Component;
import pers.yuzhyn.azylee.core.datas.datetimes.DateTimeFormat;
import pers.yuzhyn.azylee.core.datas.uuids.UUIDTool;
import pers.yuzhyn.azylee.core.logs.Alog;
import pers.yuzhyn.azylee.core.threads.sleeps.Sleep;

import javax.lang.model.element.VariableElement;
import java.time.LocalDateTime;
import java.util.Date;

@Component
@EnableScheduling
//@EnableAsync
public class TestSchedule {

    @Autowired
    SysStatusLogMapper sysStatusLogMapper;


//    @Async
    @Scheduled(cron = "0/1 * * * * *")
    public void show() {
//        Thread t = Thread.currentThread();
//        Sleep.s(2);
//        System.out.println("TestSchedule" + new Date() + " : Annotation：is show run :" + t.getId() + "," + t.getName());

        long t = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            SysStatusLog log = new SysStatusLog();
            log.setId(UUIDTool.get());
            log.setCreateTime(LocalDateTime.now());
            log.setCpu(1);
            log.setRam(100L);
            log.setDisk(500L);
            log.setAppCpu(1);
            log.setAppRam(100L);
            log.setSs(UUIDTool.getId256());
            log.setSsLong(UUIDTool.getId1024());
            int logFlag = sysStatusLogMapper.insert(log);
//            Alog.w(DateTimeFormat.toStr(LocalDateTime.now()) + "    sys-status-log: " + logFlag);
//            Alog.i();
        }
        Alog.w(DateTimeFormat.toStr(LocalDateTime.now()) + "    sys-status-log 插入数据时长: " + (System.currentTimeMillis() - t));
        Alog.i();
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
