package com.yuzhyn.bigbird.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class BigbirdAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(BigbirdAppApplication.class, args);
        log.info("============================================================");
        log.info("============================================================");
        log.info("========== bigbird 服务启动成功 ==========");
        log.info("============================================================");
        log.info("============================================================");
    }

}
