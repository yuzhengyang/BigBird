package com.yuzhyn.bigbird.app.manager;

import lombok.extern.slf4j.Slf4j;
import pers.yuzhyn.azylee.core.threads.sleeps.Sleep;

import javax.annotation.PreDestroy;

@Slf4j
public class ShutdownManager {

    /**
     * 系统优雅关闭，统一触发
     */
    @PreDestroy
    public void preDestroy() {
        long now = System.currentTimeMillis();
        log.info("==============================================");
        log.info("======== ShutdownManager 正在关闭..... ========");
        log.info("==============================================");

//        Sleep.s(10);

        log.info("==============================================");
        log.info("======== ShutdownManager 已经关闭 =============");
        log.info("==============================================");
        log.info("Time spent: " + (System.currentTimeMillis() - now));
    }
}
