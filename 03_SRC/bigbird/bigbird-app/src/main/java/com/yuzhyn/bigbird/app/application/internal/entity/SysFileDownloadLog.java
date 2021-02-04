package com.yuzhyn.bigbird.app.application.internal.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author inc
 */
@Data
public class SysFileDownloadLog {
    private String id;
    private String ip;
    private LocalDateTime createTime;
    private String cursorId;
    private String fileName;
}
