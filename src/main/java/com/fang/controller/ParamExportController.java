package com.fang.controller;

import com.fang.service.exportService.ParamExportService;
import com.fang.service.exportService.model.ExportRequestInfo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

    @PostMapping ("/downLoad")
     public ResponseEntity<MultiValueMap<String, Object>>exportTest() throws IOException {
        byte[] zipBytes = createZipFile();

        // 将字节数组包装为 ByteArrayResource
        ByteArrayResource zipResource = new ByteArrayResource(zipBytes);

        // 创建字符串内容
        String message = "This is a custom message from Spring Boot.";

        // 创建 MultiValueMap 并添加 ZIP 文件和字符串
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", zipResource);
        body.add("message", message);

        // 返回包含 ZIP 文件和字符串的 MultiValueMap
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        return  new ResponseEntity<>(body, headers, HttpStatus.OK);
    }

    private byte[] createZipFile() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            ZipEntry zipEntry = new ZipEntry("file1.txt");
            zos.putNextEntry(zipEntry);
            zos.write("Hello, this is file1".getBytes());
            zos.closeEntry();

            zipEntry = new ZipEntry("file2.txt");
            zos.putNextEntry(zipEntry);
            zos.write("Hello, this is file2".getBytes());
            zos.closeEntry();
        }
        return baos.toByteArray();
    }


}
