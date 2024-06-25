package com.fang.controller;

import com.fang.service.exportService.ParamExportService;
import com.fang.service.exportService.model.ExportRequestInfo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paramExports")
public class ParamExportController {
    @Autowired
    private ParamExportService paramExportService;

    @PostMapping("/export/{frameFlag}")
    public void exportParam(@PathVariable int frameFlag, @RequestBody ExportRequestInfo exportRequestInfo, HttpServletResponse response){
        System.out.println(frameFlag);
        System.out.println(exportRequestInfo);
        this.paramExportService.exportParam(frameFlag,exportRequestInfo,response);
    }

}
