package com.yuzhyn.bigbird.app.abarg;

import pers.yuzhyn.azylee.core.ios.dirs.DirTool;
import pers.yuzhyn.azylee.core.systems.property.SystemPropertyTool;

import java.time.LocalDateTime;

public final class R {

    public static final String AppName = "BigBird";
    public static final String AppNameCH = "Spring服务部署管理程序";
    public static final String AppNameCHShort = "服务部署";
    public static final String AppId = "";
    public static final LocalDateTime StartTime = LocalDateTime.now();
    public static String MachineId = "";
    public static long RunTimes = 0;

    public static class Paths {
        public static final String App = SystemPropertyTool.userDir();
    }

    public static class Files {
        public static final String App = "x";
        public static final String AppInfo = DirTool.combine(Paths.App, "app_info.properties");
        public static final String MachineInfo = DirTool.combine(Paths.App, "machine_info.properties");
    }
}
