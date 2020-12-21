package com.yuzhyn.bigbird.app.config;

import com.yuzhyn.bigbird.app.manager.ShutdownManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShutdownConfig {
    @Bean
    public ShutdownManager getTerminateBean() {
        return new ShutdownManager();
    }
}
