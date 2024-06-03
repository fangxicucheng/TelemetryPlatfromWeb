package com.fang.controller;

import com.fang.database.postgresql.entity.StationInfo;
import com.fang.service.stationService.StationInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stationInfo")
public class StationInfoController {
    @Autowired
    private StationInfoService stationInfoService;


    @GetMapping("/getStationNameList")
    public List<String> getStationNameList(){
        return this.stationInfoService.getStationNameList();
    }


    @GetMapping("/getAllData")
    public List<StationInfo>getStationInfoList(){
        return this.stationInfoService.getStationInfoList();
    }
    @PostMapping("/saveOrUpdate")
    public List<StationInfo> updateStationInfo(@RequestBody StationInfo stationInfo){
        this.stationInfoService.addOrUpdateStationIfo(stationInfo);
        return this.stationInfoService.getStationInfoList();
    }
    @PostMapping("/saveOrUpdateList")
    public List<StationInfo> updateStationInfoList(@RequestBody List<StationInfo>stationInfoList){
        this.stationInfoService.addOrUpdateStationInfoList(stationInfoList);
        return this.stationInfoService.getStationInfoList();
    }

    @DeleteMapping("/delete/{id}")
    public List<StationInfo> deleteStationInfo(@PathVariable Integer id){
        this.stationInfoService.deleteStationInfo(id);
        return this.stationInfoService.getStationInfoList();
    }





}
