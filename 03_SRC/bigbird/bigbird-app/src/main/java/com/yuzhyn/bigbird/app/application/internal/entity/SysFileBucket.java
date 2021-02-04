package com.yuzhyn.bigbird.app.application.internal.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysFileBucket {
    private String id;
    private String userId;
    private String name;
    private Boolean isOpen;
}
