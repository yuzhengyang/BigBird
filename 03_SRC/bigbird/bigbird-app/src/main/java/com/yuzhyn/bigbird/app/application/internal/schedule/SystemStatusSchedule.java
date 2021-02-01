package com.yuzhyn.bigbird.app.application.internal.schedule;

import com.yuzhyn.bigbird.app.application.internal.entity.SystemStatus;
import com.yuzhyn.bigbird.app.utils.LinuxSystemStatusTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pers.yuzhyn.azylee.core.logs.Alog;
import pers.yuzhyn.azylee.core.threads.sleeps.Sleep;

import java.util.Date;

@Slf4j
@Component
@EnableScheduling
@EnableAsync
public class SystemStatusSchedule {


    @Async
    @Scheduled(cron = "0/10 * * * * *")
    public void show() {
//        Thread t = Thread.currentThread();
        SystemStatus systemStatus = new SystemStatus();
        systemStatus.setAfk(0L);
        systemStatus.setCpu((int) LinuxSystemStatusTool.getCpuUseRatio());
        systemStatus.setRam(LinuxSystemStatusTool.getRam()[1]);
        Sleep.s(2);
        log.info(systemStatus.toString());
    }
}
