package com.yuzhyn.bigbird.app.application.internal.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysUserFileConf {
    private String userId;
    private LocalDateTime createTime;
    private LocalDateTime expiryTime;
    private Long spaceLimit;
    private Long usedSpace;
    private String urlPrefix;
}
