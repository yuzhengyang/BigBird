package com.yuzhyn.bigbird.app.application.internal.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysFileCursor {
    private String id;
    private String bucketId;
    private String fileId;
    private String fileName;
    private String userId;
    private LocalDateTime createTime;
    private Long version;
    private LocalDateTime expiryTime;
}
