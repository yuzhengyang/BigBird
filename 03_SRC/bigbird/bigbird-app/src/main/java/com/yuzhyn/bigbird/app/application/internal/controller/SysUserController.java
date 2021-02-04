package com.yuzhyn.bigbird.app.application.internal.controller;

import com.yuzhyn.bigbird.app.application.internal.entity.SysUser;
import com.yuzhyn.bigbird.app.application.internal.mapper.SysUserMapper;
import com.yuzhyn.bigbird.app.common.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.yuzhyn.azylee.core.datas.collections.MapTool;
import pers.yuzhyn.azylee.core.datas.ids.UUIDTool;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("i/sysuser")
public class SysUserController {

    @Autowired
    SysUserMapper sysUserMapper;

    @PostMapping("regist")
    public ResponseData regist(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "name", "email", "password")) {
            String name = MapTool.get(params, "name", "").toString();
            String email = MapTool.get(params, "email", "").toString();
            String password = MapTool.get(params, "password", "").toString();

            SysUser user = new SysUser();
            user.setId(UUIDTool.get());
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            user.setCreateTime(LocalDateTime.now());
            user.setStatus("Y");
            int flag = sysUserMapper.insert(user);
            if (flag > 0) {
                return ResponseData.okData("sysUser", user);
            }
        }
        return ResponseData.error();
    }

    @GetMapping("userList")
    public ResponseData userList(){
        List<SysUser> list = sysUserMapper.selectList(null);
        return ResponseData.okData(list);
    }
}
