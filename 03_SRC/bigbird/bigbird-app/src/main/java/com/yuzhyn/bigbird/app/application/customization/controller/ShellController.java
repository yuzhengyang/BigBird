package com.yuzhyn.bigbird.app.application.customization.controller;

import com.yuzhyn.bigbird.app.application.customization.model.StreamGobbler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pers.yuzhyn.azylee.core.datas.collections.MapTool;
import pers.yuzhyn.azylee.core.logs.Alog;
import pers.yuzhyn.azylee.core.systems.property.SystemTypeTool;

import java.io.File;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Map;
import java.util.concurrent.Executors;

@Slf4j
@RestController
@RequestMapping("/sh")
public class ShellController {

    @PostMapping("/exe")
    public String exe(@RequestBody Map map) {
        String cmd = MapTool.get(map, "cmd", "");
        if (!SystemTypeTool.isLinux()) {
            Alog.w("暂不支持非Linux的系统");
            return "暂不支持非Linux的系统";
        }
        try {
            String[] cmdA = {"/bin/sh", "-c", cmd};
            Process process = Runtime.getRuntime().exec(cmdA);
            LineNumberReader br = new LineNumberReader(new InputStreamReader(
                    process.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                sb.append(line).append("\n");
            }
            Alog.i(sb.toString());
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "null";
    }

    @PostMapping("/at")
    public String at() {
        try {
            String homeDirectory = System.getProperty("user.home");
            Process process = Runtime.getRuntime().exec(String.format("sh -c ls %s", homeDirectory));
            StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), System.out::println);
            Executors.newSingleThreadExecutor().submit(streamGobbler);
            int exitCode = process.waitFor();
            assert exitCode == 0;
        } catch (Exception ex) {
        }

        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command("sh", "-c", "ls");
            builder.directory(new File(System.getProperty("user.home")));
            Process process = builder.start();
            StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), System.out::println);
            Executors.newSingleThreadExecutor().submit(streamGobbler);
            int exitCode = process.waitFor();
            assert exitCode == 0;
        } catch (Exception ex) {
        }
        return "运行完成";
    }
}
