package com.fang.service.stationService;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class StationService {


    @PostConstruct
    public void init(){
        System.out.println("stationStationService Init!");
    }
}
