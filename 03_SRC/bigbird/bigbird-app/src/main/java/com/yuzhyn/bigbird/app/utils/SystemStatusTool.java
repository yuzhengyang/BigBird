package com.yuzhyn.bigbird.app.utils;

import pers.yuzhyn.azylee.core.systems.property.SystemTypeTool;

public class SystemStatusTool {

    public static float getCpuUseRatio() {
        if (SystemTypeTool.isLinux()) {
            return LinuxSystemStatusTool.getCpuUseRatio();
        }
        return 0;
    }

    public static long[] getRam() {
        if (SystemTypeTool.isLinux()) {
            return LinuxSystemStatusTool.getRam();
        }
        return new long[4];
    }
}
