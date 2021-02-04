package com.yuzhyn.bigbird.app.application.internal.controller;

import com.yuzhyn.bigbird.app.application.internal.service.IdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.yuzhyn.azylee.core.datas.ids.SnowflakeTool;

@Slf4j
@RestController
@RequestMapping({"i/id"})
public class IdController {

    @Autowired
    IdService idService;

    @GetMapping("/uuid")
    public String uuid() {
        return idService.getId();
    }

    @GetMapping("/snow")
    public String snow() {
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
