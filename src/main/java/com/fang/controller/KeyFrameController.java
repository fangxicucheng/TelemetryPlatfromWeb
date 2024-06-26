package com.fang.controller;

import com.fang.database.postgresql.entity.KeyFrame;
import com.fang.service.setExcpetionJuge.KeyFrameExceptionInfo;
import com.fang.service.setExcpetionJuge.KeyFrameService;
import com.fang.service.setExcpetionJuge.ParaConfigLineExceptionInfo;
import com.fang.telemetry.satelliteConfigModel.CheckConfigResult;
import jakarta.servlet.http.HttpServletResponse;
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
    public List<ParaConfigLineExceptionInfo> getKeyFrameParaConfigLine(@PathVariable Integer satelliteId, @PathVariable Integer keyFrameId) {
        return keyFrameService.getKeyFrameParaConfigleLine(satelliteId, keyFrameId);
    }

    @PostMapping("/upload/{satelliteId}/{keyFrameId}")
    public List<ParaConfigLineExceptionInfo> uploadKeyFrameFile(@PathVariable Integer satelliteId, @PathVariable Integer keyFrameId, @RequestBody MultipartFile file) throws IOException {
        return keyFrameService.uploadKeyFrameFile(satelliteId, keyFrameId, file);
    }

    @PostMapping("/update/{action}")
    public CheckConfigResult updateKeyFrame(@PathVariable String action, @RequestBody KeyFrameExceptionInfo keyFrameExceptionInfo) {
        return keyFrameService.updateKeyFrame(keyFrameExceptionInfo, action);
    }


    @DeleteMapping("/delete/{keyFrameId}")
    public CheckConfigResult deleteKeyFrame(@PathVariable Integer keyFrameId) {
        this.keyFrameService.deleteKeyFrameByFrameId(keyFrameId);
        return new CheckConfigResult();
    }

    @PostMapping("/download")
    public void downloadSatelliteKeyFrameInfo(@RequestBody Integer satelliteId, HttpServletResponse response) throws IOException {
        try {
            this.keyFrameService.downloadSatelliteKeyFrameInfo(satelliteId, response);
        } catch (Exception e) {
            System.out.println(satelliteId + "发生异常");
            e.printStackTrace();
        }

    }
}
