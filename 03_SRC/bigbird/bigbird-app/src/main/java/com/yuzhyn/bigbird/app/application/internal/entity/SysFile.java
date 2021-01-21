package com.yuzhyn.bigbird.app.application.internal.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysFile {
    private String id;
    private String name;
    private String ext;
    private long size;
    private String path;
    private LocalDateTime createTime;
    private String userId;
    private LocalDateTime expiryTime;
}
