package com.fang.controller;

import com.fang.service.exportService.ParamExportService;
import com.fang.service.exportService.model.ExportRequestInfo;
import com.fang.service.saveService.ReceiveRecordService;
import com.fang.utils.DownLoadUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/paramExports")
public class ParamExportController {
    @Autowired
    private ParamExportService paramExportService;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    @Value("${gorit.file.root.path}")
    private String baseDirectoryPath;

    /*    @PostMapping("/export/{frameFlag}")
        public ResponseEntity<MultiValueMap<String, Object>> exportParam(@PathVariable int frameFlag, @RequestBody ExportRequestInfo exportRequestInfo)*//* throws FileNotFoundException*//* {
        System.out.println(frameFlag);
        System.out.println(exportRequestInfo);
      return  this.paramExportService.exportParam(frameFlag,exportRequestInfo);
    }*/
    @PostMapping("/export/{frameFlag}")
    public void exportParam(@PathVariable int frameFlag, @RequestBody ExportRequestInfo exportRequestInfo,HttpServletResponse res) throws IOException//* throws FileNotFoundException*//*
     {
            System.out.println(frameFlag);
        System.out.println(exportRequestInfo);
       this.paramExportService.exportParam(frameFlag,exportRequestInfo,res);
}

    @PostMapping("/downLoad")
    public void exportTest(@RequestBody Integer ss, HttpServletResponse res) throws IOException {
        // Create Excel files


        File excelFile1 = createExcelFile("excel1.xlsx");
        File excelFile2 = createExcelFile("excel2.xlsx");
        res.setCharacterEncoding("UTF-8");
        res.setHeader("text", java.net.URLEncoder.encode("开始了"));
        // 设置Headers
        res.setHeader("Content-Type", "application/octet-stream;charset=UTF-8");
        // 设置下载的文件的名称-该方式已解决中文乱码问题
        res.setHeader("Content-Disposition",
                "attachment;filename=" + java.net.URLEncoder.encode("参数导出.zip", "UTF-8"));
        // Create a zip file
        //   ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(res.getOutputStream());
        ZipOutputStream zipOutputStream = null;
        String directory = baseDirectoryPath + "/参数导出/" + sdf.format(new Date()) + "/";
        File directoryFile = new File(directory);
        if (!directoryFile.exists()) {
            directoryFile.mkdirs();
        }
        String strZipPath = directory + "参数导出.zip";
        File zipFile = new File(strZipPath);
        try {
            // zipOutputStream=new ZipOutputStream(new FileOutputStream(zipFile));
            zipOutputStream = new ZipOutputStream(res.getOutputStream());
            addToZipFile(excelFile1, zipOutputStream);
            addToZipFile(excelFile2, zipOutputStream);
            zipOutputStream.flush();
            zipOutputStream.close();
        } catch (Exception e) {

        }
        // byte[] zipBytes = byteArrayOutputStream.toByteArray();

        // Clean up temporary files
        excelFile1.delete();
        excelFile2.delete();

        //DownLoadUtils.download(res,"参数导出.zip",strZipPath);
/*        FileInputStream is = null;
        BufferedInputStream bs = null;
        OutputStream os = null;
        res.setCharacterEncoding("UTF-8");*/
/*        try {
            File file = new File(strZipPath);
            if (!file.exists()) {
                file.createNewFile();
            }
            // 设置Headers
            res.setHeader("Content-Type", "application/octet-stream;charset=UTF-8");
            // 设置下载的文件的名称-该方式已解决中文乱码问题
            res.setHeader("Content-Disposition",
                    "attachment;filename=" + java.net.URLEncoder.encode("参数导出.zip", "UTF-8"));
            is = new FileInputStream(file);
            bs = new BufferedInputStream(is);
            os = res.getOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = bs.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }

        } finally {
            if (is != null) {
                is.close();
            }
            if (bs != null) {
                bs.close();
            }
            if (os != null) {
                os.flush();
                os.close();
            }
        }*/


        // Prepare headers
/*        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+ URLEncoder.encode("files.zip", StandardCharsets.UTF_8));
        headers.add("charset","UTF-8");
        headers.add("Custom-Header", "This is a custom string");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);*/
        //  headers.setContentDispositionFormData('attachment','files.zip',StandardCharsets.UTF_8);

        // Return zip file as response
        //  return new ResponseEntity<>(zipBytes, headers, HttpStatus.OK);
    }

    /*    @PostMapping ("/downLoad")
         public ResponseEntity<MultiValueMap<String, Object>>exportTest() throws IOException {
            byte[] zipBytes = createZipFile();

            // 将字节数组包装为 ByteArrayResource
            ByteArrayResource zipResource = new ByteArrayResource(zipBytes);

            // 创建字符串内容
            String message = "This is a custom message from Spring Boot.";

            // 创建 MultiValueMap 并添加 ZIP 文件和字符串
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("message", message);
            body.add("file", zipResource);


            // 返回包含 ZIP 文件和字符串的 MultiValueMap
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            return  new ResponseEntity<>(body, headers, HttpStatus.OK);
        }*/
    private File createExcelFile(String fileName) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        File file = new File(fileName);
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            workbook.createSheet("Sheet1");
            workbook.write(fileOut);
        }
        workbook.close();
        return file;
    }

    private void addToZipFile(File file, ZipOutputStream zipOutputStream) throws IOException {
        try (var fis = Files.newInputStream(file.toPath())) {
            ZipEntry zipEntry = new ZipEntry(file.getName());
            zipOutputStream.putNextEntry(zipEntry);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOutputStream.write(bytes, 0, length);
            }
            zipOutputStream.closeEntry();
        }
    }


}
