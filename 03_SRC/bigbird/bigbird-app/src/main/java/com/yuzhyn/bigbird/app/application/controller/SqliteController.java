package com.yuzhyn.bigbird.app.application.controller;

import com.yuzhyn.bigbird.app.application.entity.Demo;
import com.yuzhyn.bigbird.app.application.entity.User;
import com.yuzhyn.bigbird.app.application.mapper.DemoMapper;
import com.yuzhyn.bigbird.app.application.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.yuzhyn.azylee.core.datas.uuids.UUIDTool;

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
        demo.setId(UUIDTool.getShort());
        demo.setName("孙悟空");
        demo.setAge(LocalDateTime.now().getSecond());
        int flag = demoMapper.insert(demo);
        return "insert flag " + flag;
    }

    @GetMapping("/select")
    public Object select() {
        return demoMapper.selectList(null);
    }
}
