package com.yuzhyn.bigbird.app;

import com.yuzhyn.bigbird.app.aarg.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import pers.yuzhyn.azylee.core.datas.collections.MapTool;
import pers.yuzhyn.azylee.core.datas.numbers.LongTool;
import pers.yuzhyn.azylee.core.datas.strings.StringTool;
import pers.yuzhyn.azylee.core.datas.uuids.UUIDTool;
import pers.yuzhyn.azylee.core.ios.dirs.DirTool;
import pers.yuzhyn.azylee.core.ios.files.FileTool;
import pers.yuzhyn.azylee.core.ios.txts.PropertyTool;
import pers.yuzhyn.azylee.core.systems.property.SystemPropertyTool;
import pers.yuzhyn.azylee.core.systems.property.SystemTypeTool;

import java.io.InputStream;
import java.util.Map;

@Slf4j
@SpringBootApplication
public class BigbirdAppApplication {

    public static void main(String[] args) {
        if (BigbirdAppApplication.initRes() == false) return;
        if (BigbirdAppApplication.initProps() == false) return;

        SpringApplication.run(BigbirdAppApplication.class, args);
        log.info("/");
        log.info("/");
        log.info("============================================================");
        log.info("============================================================");
        log.info("bigbird 服务启动成功");
        log.info("");
        log.info("操作系统：" + SystemTypeTool.getOSname().toString());
        if (!SystemTypeTool.isLinux()) {
            log.info("环境异常：由于大部分功能仅支持Linux系统，Windows系统运行时部分功能将不可用");
        }
        log.info("运行路径：" + SystemPropertyTool.userDir());
        log.info("============================================================");
        log.info("============================================================");
        log.info("/");
        log.info("/");
    }

    /**
     * 初始化资源数据（创建目录结构，释放资源）
     */
    private static boolean initRes() {
        log.info("********** 初始化资源数据（创建目录结构，释放资源） **********");
        log.info("** 创建文件夹 **");
        DirTool.create(R.Paths.Database);
        DirTool.create(R.Paths.TempDir);
        DirTool.create(R.Paths.SysFile);
        log.info("** 设置临时文件目录（不建议使用） **");
        System.setProperty("java.io.tmpdir", R.Paths.TempDir);

        log.info("** 释放数据库文件到外部目录 **");
        try {
            ClassPathResource classPathResource = new ClassPathResource("db/bigbird_main_db.sqlite3");
            InputStream inputStream = classPathResource.getInputStream();
            FileTool.inputStreamToFile(inputStream, R.Files.MainDbFile, false);
            return true;
        } catch (Exception ex) {
            log.error("** 数据库文件释放失败，程序退出 **");
            log.error(ex.getMessage());
            return false;
        }
    }

    /**
     * 初始化配置信息
     */
    private static boolean initProps() {
        log.info("********** 初始化配置信息 **********");
        log.info("** 配置信息：MachineInfo **");
        // 读取
        Map<String, String> machineProps = PropertyTool.read(R.Files.MachineInfo);
        R.MachineId = MapTool.get(machineProps, "machine_id", "");
        if (!StringTool.ok(R.MachineId)) R.MachineId = UUIDTool.get();
        // 写出
        machineProps.put("machine_id", R.MachineId);
        machineProps.put("desc", "设备信息");
        PropertyTool.write(R.Files.MachineInfo, machineProps);

        log.info("** 配置信息：app **");
        // 读取
        Map<String, String> appProps = PropertyTool.read(R.Files.AppInfo);
        R.RunTimes = LongTool.parseFromInts(MapTool.get(appProps, "run_times", "0"), 0);
        // 写出
        appProps.put("run_times", String.valueOf(R.RunTimes + 1));
        appProps.put("desc", "程序信息");
        PropertyTool.write(R.Files.AppInfo, appProps);

        return true;
    }
}
