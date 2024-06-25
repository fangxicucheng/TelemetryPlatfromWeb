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
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    public void exportParam(int frameFlag, ExportRequestInfo exportRequestInfo, HttpServletResponse response) {
        List<ReceiveRecord> receiveRecordList = getReceiveRecordList(exportRequestInfo);
        if (receiveRecordList != null && receiveRecordList.size() > 0) {
            NeedExportInfo needExportInfo = new NeedExportInfo(exportRequestInfo.getCatalogList());
            ExportResult exportResult = new ExportResult();
            ParseExportTelemetry.exportTelemetry(exportRequestInfo.getSatelliteName(), receiveRecordList, needExportInfo, exportResult, frameFlag);
            System.out.println("开始了!");
        }
    }

    public void saveFile(String satelliteName, NeedExportInfo needExportInfo, ExportResult exportResult) {
        String directory = baseDirectoryPath + "/参数导出/" + sdf.format(new Date()) + "/" + satelliteName + "/";
        File directoryFile = new File(directory);
        if (!directoryFile.exists()) {
            directoryFile.mkdirs();
        }
        String strZipPath = directory + satelliteName + ".zip";
        File zipFile = new File(strZipPath);
        ZipOutputStream zipStream = null;
        try {
            zipStream = new ZipOutputStream(new FileOutputStream(zipFile));
            Map<String, ExportFrameTotalResult> frameTotalResultMap = exportResult.getExportResult();
            for (String frameName : frameTotalResultMap.keySet()) {
                String fileName = frameName + ".xlsx";
                ZipEntry zipEntry = new ZipEntry(fileName);
                zipStream.putNextEntry(zipEntry);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    public void writeExcel(NeedExportFrameInfo needFrameInfo, ExportFrameTotalResult frameTotalResult, OutputStream os) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); // 设置背景填充模式
        headerStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
        headerStyle.setBorderBottom(BorderStyle.THICK);
        headerStyle.setBorderTop(BorderStyle.THICK);
        headerStyle.setBorderRight(BorderStyle.THICK);
        headerStyle.setBorderLeft(BorderStyle.THICK);
        CellStyle contentStyle = workbook.createCellStyle();
        contentStyle.setBorderRight(BorderStyle.THIN);
        contentStyle.setWrapText(true);
        contentStyle.setBorderBottom(BorderStyle.THIN);
        contentStyle.setBorderLeft(BorderStyle.THIN);
        contentStyle.setBorderTop(BorderStyle.THIN);
        XSSFSheet sheet = workbook.createSheet(needFrameInfo.getFrameName());
        XSSFRow row = sheet.createRow(0);
        List<NeedExportParaCodeInfo> needExportParaCodeInfoList = needFrameInfo.getNeedExportParaCodeInfoList();
        for (int i = 0; i < needExportParaCodeInfoList.size(); i++) {
            XSSFCell codeCell = row.createCell(2 * i);
            XSSFCell nameCell = row.createCell(2 * i + 1);
            codeCell.setCellStyle(headerStyle);
            nameCell.setCellStyle(headerStyle);
            NeedExportParaCodeInfo needExportParaCodeInfo = needExportParaCodeInfoList.get(i);
            codeCell.setCellValue(needExportParaCodeInfo.getParaCode());
            nameCell.setCellValue(needExportParaCodeInfo.getParaName());
        }
        for (int i = 0; i < frameTotalResult.getReusltList().size(); i++) {
            ExportFrameSingleFrameResult singleFrameResult = frameTotalResult.getReusltList().get(i);

            row = sheet.createRow(i + 1);

            for (int j = 0; j < needExportParaCodeInfoList.size(); j++) {
                String paraCode = needExportParaCodeInfoList.get(j).getParaCode();
                SingleParaCodeResult paraResult = singleFrameResult.getSingleParaCodeResult(paraCode);
                if(paraResult!=null){
                    XSSFCell hexValueCell = row.createCell(j * 2);
                    hexValueCell.setCellStyle(contentStyle);
                    hexValueCell.setCellValue(paraResult.getHexValueStr()+"");
                    XSSFCell paraValueCell = row.createCell(j * 2 + 1);
                    paraValueCell.setCellStyle(contentStyle);
                    paraValueCell.setCellValue(paraResult.getParaValueStr());
                }
            }
        }
        workbook.write(os);

    }
}
