package com.fang.service.exportService;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.fang.database.postgresql.entity.ReceiveRecord;
import com.fang.service.exportService.exportResult.ExportFrameSingleFrameResult;
import com.fang.service.exportService.exportResult.ExportFrameTotalResult;
import com.fang.service.exportService.exportResult.ExportResult;
import com.fang.service.exportService.model.ExportRequestInfo;
import com.fang.service.exportService.needExport.NeedExportFrameInfo;
import com.fang.service.exportService.needExport.NeedExportInfo;
import com.fang.service.exportService.needExport.NeedExportParaCodeInfo;
import com.fang.service.saveService.ReceiveRecordRequestInfo;
import com.fang.service.saveService.ReceiveRecordService;
import com.fang.service.telemetryService.ParseExportTelemetry;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;

import org.springframework.stereotype.Service;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
public class ParamExportService {
    @Value("${gorit.file.root.path}")
    private String baseDirectoryPath;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    @Autowired
    private ReceiveRecordService receiveRecordService;

    public List<ReceiveRecord> getReceiveRecordList(ExportRequestInfo exportRequestInfo) {
        return receiveRecordService.getReceiveRecordList(exportRequestInfo.getSatelliteName().replaceAll("_北斗", ""), exportRequestInfo.getStationNameList(), exportRequestInfo.getStartTime(), exportRequestInfo.getEndTime());
    }

    public Page<ReceiveRecord> getReceiveRecordPage(ExportRequestInfo exportRequestInfo, int pageSize, int pageNumber) {
        ReceiveRecordRequestInfo requestInfo = new ReceiveRecordRequestInfo();
        List<String> satelliteNameList = new ArrayList<>();
        satelliteNameList.add(exportRequestInfo.getSatelliteName());
        requestInfo.setStationNameList(satelliteNameList);
        requestInfo.setStationNameList(exportRequestInfo.getStationNameList());
        requestInfo.setStartTime(exportRequestInfo.getStartTime());
        requestInfo.setEndTime(exportRequestInfo.getEndTime());
        requestInfo.setPageSize(pageSize);
        requestInfo.setPageNum(pageNumber);
        return receiveRecordService.getTelemetryReplayList(requestInfo);
    }

    public void exportParamStream(int frameFlag, ExportRequestInfo exportRequestInfo, HttpServletResponse res) throws Exception {
        boolean firstTime = false;
        int pageSize = 10;
        int pageNum = 0;
        String directory = baseDirectoryPath.replaceAll("\\\\", "/") + "参数导出/" + sdf.format(new Date()) + "/" + exportRequestInfo.getSatelliteName() + UUID.randomUUID() + "/";
        File directoryFile = new File(directory);
        if (!directoryFile.exists()) {
            directoryFile.mkdirs();
        }
        NeedExportInfo needExportInfo = new NeedExportInfo(exportRequestInfo.getCatalogList());
        int totalPageSize = 0;
        do {
            Page<ReceiveRecord> receiveRecordPage = getReceiveRecordPage(exportRequestInfo, pageSize, pageNum);
            if (receiveRecordPage.getTotalPages() == 0) {
                res.setHeader("text", java.net.URLEncoder.encode("未找到到对应的遥测文件"));
                return;
            }
            ExportResult exportResult = exportTelemetry(frameFlag, exportRequestInfo, needExportInfo, receiveRecordPage.getContent());
            if (exportResult.getExportResult() != null) {
                saveExcelFile(exportResult, directory, needExportInfo);
            }
            totalPageSize = receiveRecordPage.getTotalPages();
            pageNum++;//下一页

        } while (pageNum + 1 < totalPageSize);
        String zipFilePath = directory + exportRequestInfo.getSatelliteName() + ".zip";
        writeToZip(zipFilePath, directory);

        downLoadZipFile(res, zipFilePath, exportRequestInfo.getSatelliteName());
        System.out.println("参数导出完成");
    }

    List<List<Object>> getWriteDataList(boolean newFile, NeedExportFrameInfo needExportFrame, List<ExportFrameSingleFrameResult> reusltList) {
        List<List<Object>> writeDataList = new ArrayList<>();
        if (newFile) {
            List<Object> frameResult = new ArrayList<>();
            for (NeedExportParaCodeInfo needExportParaCodeInfo : needExportFrame.getNeedExportParaCodeInfoList()) {

                frameResult.add(needExportParaCodeInfo.getParaCode());
                frameResult.add(needExportParaCodeInfo.getParaName());
            }
            writeDataList.add(frameResult);

        }
        for (ExportFrameSingleFrameResult exportFrameSingleFrameResult : reusltList) {
            List<Object> frameResult = new ArrayList<>();
            for (NeedExportParaCodeInfo needExportParaCodeInfo : needExportFrame.getNeedExportParaCodeInfoList()) {
                SingleParaCodeResult singleParaCodeResult = exportFrameSingleFrameResult.getSingleParaCodeResult(needExportParaCodeInfo.getParaCode());
                if (singleParaCodeResult == null) {
                    frameResult.add("");
                    frameResult.add("");
                } else {
                    frameResult.add(singleParaCodeResult.getHexValueStr());
                    frameResult.add(singleParaCodeResult.getParaValueStr());
                }
            }
            writeDataList.add(frameResult);

        }
        return writeDataList;
    }

    /*    public void saveExcelFile(ExportResult exportResult, String directory, NeedExportInfo needExportInfo) throws IOException, InvalidFormatException {
            for (String frameName : exportResult.getExportResult().keySet()) {
                String fileName = directory + frameName + ".xlsx";
                ExportFrameTotalResult exportFrameTotalResult = exportResult.getExportResult().get(frameName);
                File file = new File(fileName);

                if (!file.exists()) {
                    file.createNewFile();
                }
                NeedExportFrameInfo needExportFrame = needExportInfo.getNeedExportFrame(frameName);

                if (file.length() == 0) {
                    SXSSFWorkbook workbook = new SXSSFWorkbook(2000);

                    SXSSFSheet sheet = workbook.createSheet(frameName);
                    SXSSFRow row = sheet.createRow(0);

                    for (int i = 0; i < needExportFrame.getNeedExportParaCodeInfoList().size(); i++) {
                        NeedExportParaCodeInfo needExportParaCodeInfo = needExportFrame.getNeedExportParaCodeInfoList().get(i);
                        SXSSFCell codeCell = row.createCell(2 * i);
                        codeCell.setCellValue(needExportParaCodeInfo.getParaCode());
                        SXSSFCell nameCell = row.createCell(2 * i + 1);
                        nameCell.setCellValue(needExportParaCodeInfo.getParaName());


                    }
                    FileOutputStream outputStream = new FileOutputStream(fileName);
                    workbook.write(outputStream);
                    workbook.close();
                    outputStream.close();

                } else {
                    try (FileInputStream inputStream = new FileInputStream(file.getPath()))
                    {
                        XSSFWorkbook wb =(XSSFWorkbook) WorkbookFactory.create(inputStream);

                        SXSSFWorkbook workbook = new SXSSFWorkbook( wb,2000);
                        int rowNum=0;
                        SXSSFSheet sheet = workbook.getSheetAt(0);
                        XSSFSheet xSheet = wb.getSheetAt(0);
                        Iterator<Row> iterator = xSheet.iterator();
                        while (iterator.hasNext()) {
                            rowNum = iterator.next().getRowNum() + 1;
                        }
                        SXSSFRow row=null;
                        for (ExportFrameSingleFrameResult exportFrameSingleFrameResult : exportFrameTotalResult.getReusltList()) {
                            row = sheet.createRow(rowNum);
                            for (int i = 0; i < needExportFrame.getNeedExportParaCodeInfoList().size(); i++) {
                                NeedExportParaCodeInfo needExportParaCodeInfo = needExportFrame.getNeedExportParaCodeInfoList().get(i);
                                SingleParaCodeResult singleParaCodeResult = exportFrameSingleFrameResult.getSingleParaCodeResult(needExportParaCodeInfo.getParaCode());
                                SXSSFCell hexCell = row.createCell(2 * i);
                                hexCell.setCellValue(singleParaCodeResult.getHexValueStr() + "");
                                SXSSFCell valueCell = row.createCell(2 * i + 1);
                                valueCell.setCellValue(singleParaCodeResult.getParaValueStr() + "");
                            }
                            rowNum++;
                        }
                        FileOutputStream outputStream = new FileOutputStream(fileName);
                        workbook.write(outputStream);
                        workbook.close();
                        outputStream.close();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }


    //                OPCPackage pkg = OPCPackage.open(file, PackageAccess.READ_WRITE);
    //                xb = new XSSFWorkbook(pkg);

                }

            }
        }*/
    public void saveExcelFile(ExportResult exportResult, String directory, NeedExportInfo needExportInfo) throws IOException, InvalidFormatException {
        for (String frameName : exportResult.getExportResult().keySet()) {
            String fileName = directory + frameName + ".xlsx";
            ExportFrameTotalResult exportFrameTotalResult = exportResult.getExportResult().get(frameName);
            File file = new File(fileName);
            boolean isNewFile = false;
            if (!file.exists()) {
                file.createNewFile();
                isNewFile = true;
            }
            NeedExportFrameInfo needExportFrame = needExportInfo.getNeedExportFrame(frameName);

            List<List<Object>> writeDataList = getWriteDataList(isNewFile, needExportFrame, exportFrameTotalResult.getReusltList());
            if (isNewFile) {
                EasyExcel.write(file.getPath()).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).sheet(frameName).doWrite(writeDataList);
            } else {
                ExcelWriter writer = EasyExcel.write(fileName).build();
                WriteSheet writerSheet = EasyExcel.writerSheet(frameName).build();
                writer.write(writeDataList,writerSheet);
                writer.finish();

            }


/*            if (file.length() == 0) {
                SXSSFWorkbook workbook = new SXSSFWorkbook(2000);

                SXSSFSheet sheet = workbook.createSheet(frameName);
                SXSSFRow row = sheet.createRow(0);

                for (int i = 0; i < needExportFrame.getNeedExportParaCodeInfoList().size(); i++) {
                    NeedExportParaCodeInfo needExportParaCodeInfo = needExportFrame.getNeedExportParaCodeInfoList().get(i);
                    SXSSFCell codeCell = row.createCell(2 * i);
                    codeCell.setCellValue(needExportParaCodeInfo.getParaCode());
                    SXSSFCell nameCell = row.createCell(2 * i + 1);
                    nameCell.setCellValue(needExportParaCodeInfo.getParaName());


                }
                FileOutputStream outputStream = new FileOutputStream(fileName);
                workbook.write(outputStream);
                workbook.close();
                outputStream.close();

            } else {
                try (FileInputStream inputStream = new FileInputStream(file.getPath())) {
                    XSSFWorkbook wb = (XSSFWorkbook) WorkbookFactory.create(inputStream);

                    SXSSFWorkbook workbook = new SXSSFWorkbook(wb, 2000);
                    int rowNum = 0;
                    SXSSFSheet sheet = workbook.getSheetAt(0);
                    XSSFSheet xSheet = wb.getSheetAt(0);
                    Iterator<Row> iterator = xSheet.iterator();
                    while (iterator.hasNext()) {
                        rowNum = iterator.next().getRowNum() + 1;
                    }
                    SXSSFRow row = null;
                    for (ExportFrameSingleFrameResult exportFrameSingleFrameResult : exportFrameTotalResult.getReusltList()) {
                        row = sheet.createRow(rowNum);
                        for (int i = 0; i < needExportFrame.getNeedExportParaCodeInfoList().size(); i++) {
                            NeedExportParaCodeInfo needExportParaCodeInfo = needExportFrame.getNeedExportParaCodeInfoList().get(i);
                            SingleParaCodeResult singleParaCodeResult = exportFrameSingleFrameResult.getSingleParaCodeResult(needExportParaCodeInfo.getParaCode());
                            SXSSFCell hexCell = row.createCell(2 * i);
                            hexCell.setCellValue(singleParaCodeResult.getHexValueStr() + "");
                            SXSSFCell valueCell = row.createCell(2 * i + 1);
                            valueCell.setCellValue(singleParaCodeResult.getParaValueStr() + "");
                        }
                        rowNum++;
                    }
                    FileOutputStream outputStream = new FileOutputStream(fileName);
                    workbook.write(outputStream);
                    workbook.close();
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }


//                OPCPackage pkg = OPCPackage.open(file, PackageAccess.READ_WRITE);
//                xb = new XSSFWorkbook(pkg);

            }*/
        }
    }

    public void writeToZip(String zipFilePath, String directoryPath) throws IOException {

        FileOutputStream fs = new FileOutputStream(zipFilePath);
        ZipOutputStream zipStream = new ZipOutputStream(fs);
        File directory = new File(directoryPath);
        List<File> fileList = Arrays.stream(directory.listFiles()).filter(t -> t.getName().contains(".xlsx")).collect(Collectors.toList());
        if (fileList != null && fileList.size() > 0) {
            for (File file : fileList) {
                ZipEntry zipEntry = new ZipEntry(file.getName());
                zipStream.putNextEntry(zipEntry);
                FileInputStream in = new FileInputStream(file);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    zipStream.write(buffer, 0, length);
                    zipStream.flush();
                }
                in.close();
                zipStream.closeEntry();
            }
        }
        zipStream.close();
        fs.close();
        // zipStream.close();
    }

    private void downLoadZipFile(HttpServletResponse res, String zipPath, String satelliteName) throws Exception {
        res.setHeader("text", java.net.URLEncoder.encode("导出成功"));
        res.setCharacterEncoding("UTF-8");
        // 设置Headers
        res.setHeader("Content-Type", "application/octet-stream;charset=UTF-8");
        // 设置下载的文件的名称-该方式已解决中文乱码问题
        res.setHeader("Content-Disposition",
                "attachment;filename=" + java.net.URLEncoder.encode(satelliteName + ".zip", "UTF-8"));

        FileInputStream in = new FileInputStream(new File(zipPath));
        byte[] buffer = new byte[1024 * 1024];
        int len = -1;
        ServletOutputStream os = res.getOutputStream();
        while ((len = in.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        in.close();
        os.close();

    }

    /*刪除文件夾*/
    private void deleteDirectory(String directory) throws IOException {
        FileUtils.deleteDirectory(new File(directory));
    }


    public ExportResult exportTelemetry(int frameFlag, ExportRequestInfo exportRequestInfo, NeedExportInfo needExportInfo, List<ReceiveRecord> receiveRecordList) {
        ExportResult exportResult = new ExportResult();
        ParseExportTelemetry.exportTelemetry(exportRequestInfo.getSatelliteName(), receiveRecordList, needExportInfo, exportResult, frameFlag);
        return exportResult;
    }

    public void exportParam(int frameFlag, ExportRequestInfo exportRequestInfo, HttpServletResponse res) throws IOException /*throws FileNotFoundException*/ {

        List<ReceiveRecord> receiveRecordList = getReceiveRecordList(exportRequestInfo);
        if (receiveRecordList != null && receiveRecordList.size() > 0) {
            res.setHeader("text", java.net.URLEncoder.encode("导出成功"));
            res.setCharacterEncoding("UTF-8");
            // 设置Headers
            res.setHeader("Content-Type", "application/octet-stream;charset=UTF-8");
            // 设置下载的文件的名称-该方式已解决中文乱码问题
            res.setHeader("Content-Disposition",
                    "attachment;filename=" + java.net.URLEncoder.encode(exportRequestInfo.getSatelliteName() + ".zip", "UTF-8"));
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
            saveFile(needExportInfo, exportResult, res.getOutputStream());

        } else {
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

    public void saveFile(NeedExportInfo needExportInfo, ExportResult exportResult, OutputStream os) {
        try (ZipOutputStream zipStream = new ZipOutputStream(os)) {
            Map<String, ExportFrameTotalResult> frameTotalResultMap = exportResult.getExportResult();
            for (String frameName : needExportInfo.getNeedExportFrameMap().keySet()) {
                NeedExportFrameInfo needExportFrame = needExportInfo.getNeedExportFrame(frameName);
                String fileName = frameName + ".xlsx";
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                writeExcel(needExportFrame, frameTotalResultMap.get(frameName), baos);
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
            codeCell.setCellValue(needExportParaCodeInfo.getParaCode() + "");
            nameCell.setCellValue(needExportParaCodeInfo.getParaName() + "");
        }
        if (frameTotalResult != null) {
            for (int i = 0; i < frameTotalResult.getReusltList().size(); i++) {
                ExportFrameSingleFrameResult singleFrameResult = frameTotalResult.getReusltList().get(i);
                row = sheet.createRow(i + 1);
                for (int j = 0; j < needExportParaCodeInfoList.size(); j++) {
                    String paraCode = needExportParaCodeInfoList.get(j).getParaCode();
                    SingleParaCodeResult paraResult = singleFrameResult.getSingleParaCodeResult(paraCode);
                    if (paraResult != null) {
                        XSSFCell hexValueCell = row.createCell(j * 2);
                        //    hexValueCell.setCellStyle(contentStyle);
                        hexValueCell.setCellValue(paraResult.getHexValueStr() + "");
                        XSSFCell paraValueCell = row.createCell(j * 2 + 1);
                        //  paraValueCell.setCellStyle(contentStyle);
                        paraValueCell.setCellValue(paraResult.getParaValueStr() + "");
                    }
                }
            }
        }
        workbook.write(os);
        workbook.close();

    }
}
