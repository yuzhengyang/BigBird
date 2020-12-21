package com.yuzhyn.bigbird.app.application.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.yuzhyn.azylee.core.datas.ids.SnowflakeTool;

import java.io.*;
import java.net.InetAddress;
import java.util.StringTokenizer;

@RestController
@RequestMapping("/ls")
public class LinuxStatusController {

    @GetMapping("/ram")
    public String get() {
        String ip = "";
        try {
            InetAddress inet = InetAddress.getLocalHost();
            ip = inet.toString().substring(inet.toString().indexOf("/") + 1);
        } catch (Exception ex) {
        }


        int[] memInfo = this.getMemInfo();

        StringBuilder sb = new StringBuilder();
        sb.append("ip:  " + ip);
        sb.append("cpuinfo:  " + String.valueOf(this.getCpuInfo()));

        if (memInfo != null && memInfo.length > 1) {
            sb.append("MemTotal:  " + String.valueOf(memInfo[0]));
            sb.append("MemFree:  " + String.valueOf(memInfo[1]));
        }
        return sb.toString();
    }

    /**
     * get memory by used info
     *
     * @return float efficiency
     * @throws IOException
     * @throws InterruptedException
     */
    public float getCpuInfo() {
        try {
            File file = new File("/proc/stat");
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file)));
            StringTokenizer token = new StringTokenizer(br.readLine());
            token.nextToken();
            int user1 = Integer.parseInt(token.nextToken());
            int nice1 = Integer.parseInt(token.nextToken());
            int sys1 = Integer.parseInt(token.nextToken());
            int idle1 = Integer.parseInt(token.nextToken());

            Thread.sleep(1000);

            br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file)));
            token = new StringTokenizer(br.readLine());
            token.nextToken();
            int user2 = Integer.parseInt(token.nextToken());
            int nice2 = Integer.parseInt(token.nextToken());
            int sys2 = Integer.parseInt(token.nextToken());
            int idle2 = Integer.parseInt(token.nextToken());

            return (float) ((user2 + sys2 + nice2) - (user1 + sys1 + nice1))
                    / (float) ((user2 + nice2 + sys2 + idle2) - (user1 + nice1
                    + sys1 + idle1));
        } catch (Exception ex) {
        }
        return 0;
    }

    public int[] getMemInfo() {
        try {
            File file = new File("/proc/meminfo");
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file)));
            int[] result = new int[4];
            String str = null;
            StringTokenizer token = null;
            while ((str = br.readLine()) != null) {
                token = new StringTokenizer(str);
                if (!token.hasMoreTokens())
                    continue;

                str = token.nextToken();
                if (!token.hasMoreTokens())
                    continue;

                if (str.equalsIgnoreCase("MemTotal:"))
                    result[0] = Integer.parseInt(token.nextToken());
                else if (str.equalsIgnoreCase("MemFree:"))
                    result[1] = Integer.parseInt(token.nextToken());
                else if (str.equalsIgnoreCase("SwapTotal:"))
                    result[2] = Integer.parseInt(token.nextToken());
                else if (str.equalsIgnoreCase("SwapFree:"))
                    result[3] = Integer.parseInt(token.nextToken());
            }

            return result;
        } catch (Exception ex) {
        }
        return null;
    }
}
