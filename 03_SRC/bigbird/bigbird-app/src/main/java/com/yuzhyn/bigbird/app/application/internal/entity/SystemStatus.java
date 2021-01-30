package com.yuzhyn.bigbird.app.application.internal.entity;

import lombok.Data;
import pers.yuzhyn.azylee.core.datas.datetimes.DateTimeFormat;
import pers.yuzhyn.azylee.core.datas.datetimes.DateTimeFormatPattern;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
public class SystemStatus {

    //2021-01-28 23:59:03|58|58|14|4024815|43740103|0|62943

    private LocalDateTime startTime;
    private LocalDateTime stopTime;
    private Long time;
    private Long afk;
    private Integer cpu;
    private Long ram;
    private Long disk;
    private Integer appCpu;
    private Long appRam;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(DateTimeFormat.toStr(startTime));
        stringBuilder.append("|");
        stringBuilder.append(Duration.between(startTime, stopTime).getSeconds());
        stringBuilder.append("|");
        stringBuilder.append(Duration.between(startTime, stopTime).getSeconds());
        stringBuilder.append("|");
        stringBuilder.append(cpu);
        stringBuilder.append("|");
        stringBuilder.append(ram);
        stringBuilder.append("|");
        stringBuilder.append(disk);
        stringBuilder.append("|");
        stringBuilder.append(appCpu);
        stringBuilder.append("|");
        stringBuilder.append(appRam);
        return stringBuilder.toString();
    }
}
