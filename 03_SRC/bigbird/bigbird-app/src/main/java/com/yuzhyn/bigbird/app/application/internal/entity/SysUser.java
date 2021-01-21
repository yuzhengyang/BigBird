package com.yuzhyn.bigbird.app.application.internal.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_user")
public class SysUser {

    @TableId("id")
    private String id;

    @TableField("name")
    private String name;

    private String email;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String status;

    private String password;
}
