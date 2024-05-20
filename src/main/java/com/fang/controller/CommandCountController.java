package com.fang.controller;

import com.fang.database.postgresql.entity.CommandCount;
import com.fang.service.commandCountService.CommandCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/commandCount")
public class CommandCountController {
    @Autowired
    private CommandCountService commandCountService;

    @GetMapping("/getAllData")
    public List<CommandCount> getCommandCountList() {
        return this.commandCountService.getCommandCountList();
    }


    @PostMapping("/saveOrUpdate")
    public List<CommandCount> saveOrUpdateCommandCount(@RequestBody CommandCount commandCount) {
        this.commandCountService.saveOrUpdateCommandCount(commandCount);
        return this.commandCountService.getCommandCountList();

    }
    @PostMapping("/saveOrUpdateList")
    public List<CommandCount> saveOrUpdateCommandCountList(@RequestBody List<CommandCount> commandCountList) {
        this.commandCountService.saveOrUpdateCommandCountList(commandCountList);
        return this.commandCountService.getCommandCountList();

    }
    @DeleteMapping("/delete/{id}")
    public List<CommandCount>deleteCommandCountById(@PathVariable Integer id){

        this.commandCountService.deleteCommandCountById(id);
        return this.commandCountService.getCommandCountList();
    }

}
