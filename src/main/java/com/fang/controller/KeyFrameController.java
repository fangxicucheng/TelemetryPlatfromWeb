package com.fang.controller;

import com.fang.database.postgresql.entity.KeyFrame;
import com.fang.service.setExcpetionJuge.KeyFrameService;
import com.fang.service.setExcpetionJuge.ParaConfigLineExceptionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/keyFrame")
public class KeyFrameController {
    @Autowired
    private KeyFrameService keyFrameService;
    @GetMapping("/{satelliteId}")
    public List<KeyFrame> getKeyFrameListBySatelliteId(@PathVariable Integer satelliteId) {
        return this.keyFrameService.getKeyFrameListBySatelliteId(satelliteId);
    }

    @GetMapping("/{satelliteId}/{keyFrameId}")
    public List<ParaConfigLineExceptionInfo> getKeyFrameParaConfigLine(@PathVariable Integer satelliteId,@PathVariable Integer keyFrameId){
        return keyFrameService.getKeyFrameParaConfigleLine(satelliteId,keyFrameId);
    }
    @PostMapping("/upload/{satelliteId}/{keyFrameId}")
    public List<ParaConfigLineExceptionInfo> uploadKeyFrameFile(@PathVariable Integer satelliteId, @PathVariable Integer keyFrameId , @RequestBody MultipartFile file) throws IOException {
        return keyFrameService.uploadKeyFrameFile(satelliteId,keyFrameId,file);
    }
}
