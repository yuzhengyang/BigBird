package com.yuzhyn.bigbird.app.application.customization.controller;

import com.yuzhyn.bigbird.app.application.customization.entity.Demo;
import com.yuzhyn.bigbird.app.application.customization.entity.User;
import com.yuzhyn.bigbird.app.application.customization.mapper.DemoMapper;
import com.yuzhyn.bigbird.app.application.customization.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.yuzhyn.azylee.core.datas.uuids.UUIDTool;
import pers.yuzhyn.azylee.core.logs.Alog;

import java.io.File;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/sqlite")
public class SqliteController {

    @Autowired
    DemoMapper demoMapper;

    @Autowired
    UserMapper userMapper;

    @RequestMapping("/insert2")
    public Object insert2(String name) {
        User user = new User();
        user.setName(name);
        userMapper.insert(user);
        return user;
    }

    @RequestMapping("/show2")
    public Object show2() {

        return userMapper.selectList(null);
    }

    @GetMapping("/insert")
    public String insert() {
        Demo demo = new Demo();
        demo.setId(UUIDTool.get());
        demo.setName("孙悟空");
        demo.setAge(LocalDateTime.now().getSecond());
        int flag = demoMapper.insert(demo);
        return "insert flag " + flag;
    }

    @GetMapping("/select")
    public Object select() {
        File f = new File(this.getClass().getResource("/").getPath());
        Alog.e(f.getPath());
        Alog.e(f.getAbsolutePath());
        return demoMapper.selectList(null);
    }

    public static void main(String[] args) {
        Alog.e(System.getProperty("java.io.tmpdir"));
    }

}
