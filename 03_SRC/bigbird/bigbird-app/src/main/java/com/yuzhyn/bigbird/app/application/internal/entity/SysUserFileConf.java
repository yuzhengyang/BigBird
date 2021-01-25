package com.yuzhyn.bigbird.app.application.internal.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysUserFileConf {

    @TableId("user_id")
    private String userId;
    private LocalDateTime createTime;
    private LocalDateTime expiryTime;
    private Long spaceLimit;
    private Long usedSpace;
    private String urlPrefix;
}
