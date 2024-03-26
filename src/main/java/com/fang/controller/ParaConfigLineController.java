package com.fang.controller;

import com.fang.database.postgresql.entity.ParaConfigLineDb;
import com.fang.service.setSatelliteConfig.ParaConfigLineConfigService;
import com.fang.telemetry.satelliteConfigModel.TeleParaConfigLineDbModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/paraConfigLine")
public class ParaConfigLineController {

    @Autowired
    private ParaConfigLineConfigService paraConfigLineConfigService;

    @GetMapping("/{frameId}")
    public List<TeleParaConfigLineDbModel> getParaConfigLineList(@PathVariable  Integer frameId){
        return this.paraConfigLineConfigService.getTeleParaConfigLineDbModelList(frameId);
    }



    @PostMapping("/updateParaConfigLineList/{frameId}")
    public List<TeleParaConfigLineDbModel> updateParaConfigLineList(@PathVariable  Integer frameId,@RequestBody List<TeleParaConfigLineDbModel> paraconfigLineList){
       return this.paraConfigLineConfigService.updateTeleParaConfigLineList(frameId,paraconfigLineList);
    }

    @PostMapping("/updateParaConfigLineList")
   // @RequestBody List<TeleParaConfigLineDbModel> paraconfigLineList
    public String updateParaConfigLineList1 (){
        System.out.println("66");
        return" this.paraConfigLineConfigService.updateTeleParaConfigLineList(frameId,paraconfigLineList)";
    }


}
