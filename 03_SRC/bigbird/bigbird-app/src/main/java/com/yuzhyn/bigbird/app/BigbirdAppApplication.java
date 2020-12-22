package com.yuzhyn.bigbird.app;

import com.yuzhyn.bigbird.app.utils.SystemTypeTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class BigbirdAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(BigbirdAppApplication.class, args);
        log.info("/");
        log.info("/");
        log.info("============================================================");
        log.info("============================================================");
        log.info("bigbird 服务启动成功");
        log.info("运行环境信息：");
        log.info("操作系统：" + SystemTypeTool.getOSname().toString() + " 当前仅支持Linux系统：" + SystemTypeTool.isLinux());
        log.info("============================================================");
        log.info("============================================================");
        log.info("/");
        log.info("/");
    }

}
