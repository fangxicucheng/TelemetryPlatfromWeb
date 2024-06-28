package com.fang.service.exportService;

import com.fang.database.postgresql.entity.ReceiveRecord;
import com.fang.service.exportService.exportResult.ExportFrameSingleFrameResult;
import com.fang.service.exportService.exportResult.ExportFrameTotalResult;
import com.fang.service.exportService.exportResult.ExportResult;
import com.fang.service.exportService.model.ExportRequestInfo;
import com.fang.service.exportService.needExport.NeedExportFrameInfo;
import com.fang.service.exportService.needExport.NeedExportInfo;
import com.fang.service.exportService.needExport.NeedExportParaCodeInfo;
import com.fang.service.saveService.ReceiveRecordService;
import com.fang.service.telemetryService.ParseExportTelemetry;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
public class ParamExportService {
    @Value("${gorit.file.root.path}")
    private String baseDirectoryPath;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    @Autowired
    private ReceiveRecordService receiveRecordService;

    public List<ReceiveRecord> getReceiveRecordList(ExportRequestInfo exportRequestInfo) {
        return receiveRecordService.getReceiveRecordList(exportRequestInfo.getSatelliteName().replaceAll("_北斗", ""), exportRequestInfo.getStationNameList(), exportRequestInfo.getStartTime(), exportRequestInfo.getEndTime());
    }
    public void exportParam(int frameFlag, ExportRequestInfo exportRequestInfo, HttpServletResponse res) throws IOException /*throws FileNotFoundException*/ {

        List<ReceiveRecord> receiveRecordList = getReceiveRecordList(exportRequestInfo);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        if (receiveRecordList != null && receiveRecordList.size() > 0) {

            res.setHeader("text", java.net.URLEncoder.encode("导出成功"));
            res.setCharacterEncoding("UTF-8");

            // 设置Headers
            res.setHeader("Content-Type", "application/octet-stream;charset=UTF-8");
            // 设置下载的文件的名称-该方式已解决中文乱码问题
            res.setHeader("Content-Disposition",
                    "attachment;filename=" + java.net.URLEncoder.encode(exportRequestInfo.getSatelliteName()+".zip", "UTF-8"));
            NeedExportInfo needExportInfo = new NeedExportInfo(exportRequestInfo.getCatalogList());
            ExportResult exportResult = new ExportResult();
            ParseExportTelemetry.exportTelemetry(exportRequestInfo.getSatelliteName(), receiveRecordList, needExportInfo, exportResult, frameFlag);
//            String directory = baseDirectoryPath + "/参数导出/" + sdf.format(new Date()) + "/";
//            File directoryFile = new File(directory);
//            if (!directoryFile.exists()) {
//                directoryFile.mkdirs();
//            }
//            String strZipPath = directory +   "测试数据.zip";
//            File zipFile = new File(strZipPath);
            ByteArrayOutputStream zipBaos = new ByteArrayOutputStream();
            // FileOutputStream zipBaos=new FileOutputStream(zipFile);
            saveFile(needExportInfo,exportResult,res.getOutputStream());

        }
        else
        {
            res.setHeader("text", java.net.URLEncoder.encode("未找到到对应的遥测文件"));
           // body.add("text","未找到到对应的遥测文件！");
        }
//        HttpHeaders zipHeaders = new HttpHeaders();
//        zipHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+ exportRequestInfo.getSatelliteName().replaceAll("_北斗","")+".zip");
//        zipHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        // 设置字符串的响应头
//        HttpHeaders textHeaders = new HttpHeaders();
//        textHeaders.setContentType(MediaType.TEXT_PLAIN);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.MULTIPART_MIXED);
       // HttpHeaders headers = new HttpHeaders();
       // headers.setContentType(MediaType.MULTIPART_FORM_DATA);
       // return  new ResponseEntity<>(body, headers, HttpStatus.OK);
    }

/*    public ResponseEntity<MultiValueMap<String, Object>> exportParam(int frameFlag, ExportRequestInfo exportRequestInfo) *//*throws FileNotFoundException*//* {
        List<ReceiveRecord> receiveRecordList = getReceiveRecordList(exportRequestInfo);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        if (receiveRecordList != null && receiveRecordList.size() > 0) {
            body.add("text","导出成功！");
            NeedExportInfo needExportInfo = new NeedExportInfo(exportRequestInfo.getCatalogList());
            ExportResult exportResult = new ExportResult();
            ParseExportTelemetry.exportTelemetry(exportRequestInfo.getSatelliteName(), receiveRecordList, needExportInfo, exportResult, frameFlag);
//            String directory = baseDirectoryPath + "/参数导出/" + sdf.format(new Date()) + "/";
//            File directoryFile = new File(directory);
//            if (!directoryFile.exists()) {
//                directoryFile.mkdirs();
//            }
//            String strZipPath = directory +   "测试数据.zip";
//            File zipFile = new File(strZipPath);
           ByteArrayOutputStream zipBaos = new ByteArrayOutputStream();
           // FileOutputStream zipBaos=new FileOutputStream(zipFile);
            saveFile(needExportInfo,exportResult,zipBaos);
           body.add("file", new ByteArrayResource(zipBaos.toByteArray())*//*{
               @Override
               public String getFilename() {
                   return exportRequestInfo.getSatelliteName().replaceAll("_北斗","");
               }
           }*//*);
        }
        else
        {
            body.add("text","未找到到对应的遥测文件！");
        }
//        HttpHeaders zipHeaders = new HttpHeaders();
//        zipHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+ exportRequestInfo.getSatelliteName().replaceAll("_北斗","")+".zip");
//        zipHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        // 设置字符串的响应头
//        HttpHeaders textHeaders = new HttpHeaders();
//        textHeaders.setContentType(MediaType.TEXT_PLAIN);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.MULTIPART_MIXED);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        return  new ResponseEntity<>(body, headers, HttpStatus.OK);
    }*/

    public void saveFile( NeedExportInfo needExportInfo, ExportResult exportResult,OutputStream os) {
        try ( ZipOutputStream zipStream = new ZipOutputStream(os)){
            Map<String, ExportFrameTotalResult> frameTotalResultMap = exportResult.getExportResult();
            for (String frameName : frameTotalResultMap.keySet())
            {
                NeedExportFrameInfo needExportFrame = needExportInfo.getNeedExportFrame(frameName);
                String fileName = frameName + ".xlsx";
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                writeExcel(needExportFrame,frameTotalResultMap.get(frameName),baos);
                ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                ZipEntry zipEntry = new ZipEntry(fileName);
                zipStream.putNextEntry(zipEntry);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = bais.read(buffer)) > 0) {
                    zipStream.write(buffer, 0, length);
                }
                zipStream.closeEntry();
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    public void writeExcel(NeedExportFrameInfo needFrameInfo, ExportFrameTotalResult frameTotalResult, OutputStream os) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
//        CellStyle headerStyle = workbook.createCellStyle();
//        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); // 设置背景填充模式
//        headerStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
//        headerStyle.setBorderBottom(BorderStyle.THICK);
//        headerStyle.setBorderTop(BorderStyle.THICK);
//        headerStyle.setBorderRight(BorderStyle.THICK);
//        headerStyle.setBorderLeft(BorderStyle.THICK);
//        CellStyle contentStyle = workbook.createCellStyle();
//        contentStyle.setBorderRight(BorderStyle.THIN);
//        contentStyle.setWrapText(true);
//        contentStyle.setBorderBottom(BorderStyle.THIN);
//        contentStyle.setBorderLeft(BorderStyle.THIN);
//        contentStyle.setBorderTop(BorderStyle.THIN);
        XSSFSheet sheet = workbook.createSheet(needFrameInfo.getFrameName());
        XSSFRow row = sheet.createRow(0);
        List<NeedExportParaCodeInfo> needExportParaCodeInfoList = needFrameInfo.getNeedExportParaCodeInfoList();
        for (int i = 0; i < needExportParaCodeInfoList.size(); i++) {
            XSSFCell codeCell = row.createCell(2 * i);
            XSSFCell nameCell = row.createCell(2 * i + 1);
          //  codeCell.setCellStyle(headerStyle);
          //  nameCell.setCellStyle(headerStyle);
            NeedExportParaCodeInfo needExportParaCodeInfo = needExportParaCodeInfoList.get(i);
            codeCell.setCellValue(needExportParaCodeInfo.getParaCode()+"");
            nameCell.setCellValue(needExportParaCodeInfo.getParaName()+"");
        }
        for (int i = 0; i < frameTotalResult.getReusltList().size(); i++) {
            ExportFrameSingleFrameResult singleFrameResult = frameTotalResult.getReusltList().get(i);
            row = sheet.createRow(i + 1);
            for (int j = 0; j < needExportParaCodeInfoList.size(); j++) {
                String paraCode = needExportParaCodeInfoList.get(j).getParaCode();
                SingleParaCodeResult paraResult = singleFrameResult.getSingleParaCodeResult(paraCode);
                if(paraResult!=null){
                    XSSFCell hexValueCell = row.createCell(j * 2);
                //    hexValueCell.setCellStyle(contentStyle);
                    hexValueCell.setCellValue(paraResult.getHexValueStr()+"");
                    XSSFCell paraValueCell = row.createCell(j * 2 + 1);
                  //  paraValueCell.setCellStyle(contentStyle);
                    paraValueCell.setCellValue(paraResult.getParaValueStr()+"");
                }
            }
        }
        workbook.write(os);
        workbook.close();

    }
}
