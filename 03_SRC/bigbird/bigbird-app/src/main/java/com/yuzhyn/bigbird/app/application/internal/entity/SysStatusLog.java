package com.yuzhyn.bigbird.app.application.internal.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysStatusLog {
    private String id;
    private LocalDateTime createTime;
    private Integer cpu;
    private Long ram;
    private Long disk;
    private Integer appCpu;
    private Long appRam;
    private String ss;
    private String ssLong;
}
