package com.yuzhyn.bigbird.app;

import com.yuzhyn.bigbird.app.abarg.MyPath;
import com.yuzhyn.bigbird.app.abarg.R;
import com.yuzhyn.bigbird.app.utils.SystemTypeTool;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pers.yuzhyn.azylee.core.datas.collections.MapTool;
import pers.yuzhyn.azylee.core.datas.numbers.LongTool;
import pers.yuzhyn.azylee.core.datas.strings.StringTool;
import pers.yuzhyn.azylee.core.datas.uuids.UUIDTool;
import pers.yuzhyn.azylee.core.ios.txts.PropertyTool;
import pers.yuzhyn.azylee.core.systems.property.SystemPropertyTool;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootApplication
public class BigbirdAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(BigbirdAppApplication.class, args);
        log.info("/");
        log.info("/");
        log.info("============================================================");
        log.info("============================================================");
        log.info("bigbird 服务启动成功");
        log.info("运行环境信息：");
        log.info("操作系统：" + SystemTypeTool.getOSname().toString());
        if (!SystemTypeTool.isLinux()) {
            log.info("（仅支持Linux系统，部分功能将不可用）");
        }
        log.info("运行路径：" + SystemPropertyTool.userDir());
        System.out.println(MyPath.getRealPath());
        System.out.println(MyPath.getProjectPath());
        log.info("============================================================");
        log.info("initProps");
        initProps();
        log.info("============================================================");
        log.info("============================================================");
        log.info("/");
        log.info("/");
    }

    private static void initProps() {
        // 配置信息：MachineInfo
        // 读取
        Map<String, String> machineProps = PropertyTool.read(R.Files.MachineInfo);
        R.MachineId = MapTool.get(machineProps, "machine_id", "");
        if (!StringTool.ok(R.MachineId)) R.MachineId = UUIDTool.get();
        // 写出
        machineProps.put("machine_id", R.MachineId);
        machineProps.put("desc", "设备信息");
        PropertyTool.write(R.Files.MachineInfo, machineProps);

        // 配置信息：app
        // 读取
        Map<String, String> appProps = PropertyTool.read(R.Files.AppInfo);
        R.RunTimes = LongTool.parseFromInts(MapTool.get(appProps, "run_times", "0"), 0);
        // 写出
        appProps.put("run_times", String.valueOf(R.RunTimes + 1));
        appProps.put("desc", "程序信息");
        PropertyTool.write(R.Files.AppInfo, appProps);
    }
}
