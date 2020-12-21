package com.yuzhyn.bigbird.app.application.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping("/guid")
public class GuidController {

    @GetMapping("/get")
    public String get() {
        return UUID.randomUUID().toString();
    }

//    @GetMapping("/get/{count}")
//    public ResponseData get(@PathVariable("count") int count) {
//        ArrayList<String> ids = new ArrayList();
//        if (count <= 0) {
//            count = 1;
//        }
//        for (int i = 0; i < count; i++) {
//            ids.add(UUID.randomUUID().toString());
//        }
//        return ResponseData.ok(ids);
//    }
}
