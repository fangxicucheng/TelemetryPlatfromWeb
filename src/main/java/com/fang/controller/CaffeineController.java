package com.fang.controller;

import com.fang.service.caffeineService.CaffeineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/caches")
public class CaffeineController {
    @Autowired
    private CaffeineService caffeineService;
    @GetMapping("/{recordId}/{serialNum}")
    public String testGet(@PathVariable int recordId,@PathVariable int serialNum) {

        return caffeineService.testGet(recordId,serialNum);

    }

}
