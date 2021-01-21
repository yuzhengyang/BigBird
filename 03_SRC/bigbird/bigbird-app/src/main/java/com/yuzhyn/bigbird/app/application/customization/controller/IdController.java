package com.yuzhyn.bigbird.app.application.customization.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.yuzhyn.azylee.core.datas.ids.SnowflakeTool;

@RestController
@RequestMapping("/id")
public class IdController {

    @GetMapping("/get")
    public String get() {
        SnowflakeTool idWorker = new SnowflakeTool(1, 1, 1);
        return String.valueOf(idWorker.nextId());
    }

//    @GetMapping("/get/{count}")
//    public ResponseData get(@PathVariable("count") int count) {
//        ArrayList<Long> ids = new ArrayList();
//        SnowflakeTool idWorker = new SnowflakeTool(1, 1, 1);
//        if (count <= 0) {
//            count = 1;
//        }
//        for (int i = 0; i < count; i++) {
//            ids.add(idWorker.nextId());
//        }
//        return ResponseData.ok(ids);
//    }
}
