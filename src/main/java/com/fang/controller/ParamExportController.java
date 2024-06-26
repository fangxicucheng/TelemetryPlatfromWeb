package com.fang.controller;

import com.fang.service.exportService.ParamExportService;
import com.fang.service.exportService.model.ExportRequestInfo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/paramExports")
public class ParamExportController {
    @Autowired
    private ParamExportService paramExportService;

    @PostMapping("/export/{frameFlag}")
    public ResponseEntity<MultiValueMap<String, Object>> exportParam(@PathVariable int frameFlag, @RequestBody ExportRequestInfo exportRequestInfo)/* throws FileNotFoundException*/ {
        System.out.println(frameFlag);
        System.out.println(exportRequestInfo);
      return  this.paramExportService.exportParam(frameFlag,exportRequestInfo);
    }

}
