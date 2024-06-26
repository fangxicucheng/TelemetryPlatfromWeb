package com.fang.utils;

import com.fang.telemetry.ExcelCellListSelectShow;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelUtils {
    /**
     * 1、将单元格的内容转换为字符串
     *
     * @param cell 单元格
     * @return 返回转换后的字符串
     */
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static String baseDirectoryPath = "D:\\卫星遥测数据监控平台\\";

    private static String convertCellValueToString(Cell cell,FormulaEvaluator formulaEvaluator) {
        //1.1、判断单元格的数据是否为空
        if (cell == null) {
            return null;
        }
        //1.2、设置单元格数据的初始值
        String cellValue = null;
        //1.3、获取单元格数据的类型
        switch (cell.getCellType()) {
            case NUMERIC:
                //1.3.1、获取到单元格数据的格式
                short dataFormat = cell.getCellStyle().getDataFormat();
                if (DateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf = null;
                    //1.3.1.1、处理日期格式，根据不同日期长度去判断
                    switch (dataFormat) {
                        case 14:
                            sdf = new SimpleDateFormat("yyyy/MM/dd");
                            break;
                        case 21:
                            sdf = new SimpleDateFormat("HH:mm:ss");
                            break;
                        case 22:
                            sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                            break;
                    }
                    //1.3.1.2、处理时间格式
                    Date date = cell.getDateCellValue();
                    assert sdf != null;
                    cellValue = sdf.format(date);
                } else if (dataFormat == 0) {
                    //1.3.2、处理普通数字格式
                    DecimalFormat format = new DecimalFormat("0");
                    double numericCellValue = cell.getNumericCellValue();
                    cellValue = format.format(numericCellValue);
                }
                break;
            case STRING:
            case BLANK:
                //处理字符串类型
                cellValue = cell.getStringCellValue();
                break;
            case BOOLEAN:
                //处理布尔类型
                boolean booleanCellValue = cell.getBooleanCellValue();
                cellValue = Boolean.toString(booleanCellValue);
                break;
            case FORMULA:
                //处理函数类型
                cellValue = String.valueOf((int)(formulaEvaluator.evaluate(cell).getNumberValue()));
                break;
            case ERROR:
                byte errorCellValue = cell.getErrorCellValue();
                cellValue = Byte.toString(errorCellValue);
                break;
            default:
                break;
        }
        return cellValue;
    }

    /**
     * 2、处理合并单元格里面的数据
     *
     * @param sheet 工作薄
     * @return 返回合并单元格后里面的数据
     */
    public static List<CellRangeAddress> getCombineCell(Sheet sheet) {
        List<CellRangeAddress> list = new ArrayList<CellRangeAddress>();
        //2.1、获得一个 sheet 中合并单元格的数量
        int sheetMergerCount = sheet.getNumMergedRegions();
        //2.2、遍历合并单元格
        for (int i = 0; i < sheetMergerCount; i++) {
            //2.2.1、获得合并单元格加入list中
            CellRangeAddress rangeAddress = sheet.getMergedRegion(i);
            list.add(rangeAddress);
        }
        return list;
    }

    /**
     * 3、判断单元格是否为合并单元格
     *
     * @param listCombineCell 存放合并单元格的list
     * @param cell            需要判断的单元格
     * @param sheet           sheet
     */
    public static String isCombineCell(List<CellRangeAddress> listCombineCell, Cell cell, Sheet sheet,FormulaEvaluator formulaEvaluator) {
        //3.1、设置第一个单元格和最后一个单元格的值
        int firstColumn = 0;
        int lastColumn = 0;
        //3.2、设置第一个单元格和最后一个行的值
        int firstRow = 0;
        int lastRow = 0;
        //3.3、初始化单元格值
        String cellValue = null;
        for (CellRangeAddress rangeAddress : listCombineCell) {
            //3.3.1、获得合并单元格的起始行, 结束行, 起始列, 结束列
            firstColumn = rangeAddress.getFirstColumn();
            lastColumn = rangeAddress.getLastColumn();
            firstRow = rangeAddress.getFirstRow();
            lastRow = rangeAddress.getLastRow();
            //3.3.2、判断是不是合并单元格
            if (cell.getRowIndex() >= firstRow && cell.getRowIndex() <= lastRow) {
                if (cell.getColumnIndex() >= firstColumn && cell.getColumnIndex() <= lastColumn) {
                    //3.3.2.1、获取行数据
                    Row fRow = sheet.getRow(firstRow);
                    //3.3.2.2、获取单元格数据
                    Cell fCell = fRow.getCell(firstColumn);
                    //3.3.2.3、对有合并单元格的数据进行格式处理
                    cellValue = convertCellValueToString(fCell,formulaEvaluator);
                    break;
                }
            } else {
                //3.3.3、对没有合并单元格的数据进行格式处理
                cellValue = convertCellValueToString(cell,formulaEvaluator);
            }
        }
        //3.4、返回处理后的单元格数据
        return cellValue;
    }

    /**
     * 4、判断sheet页中是否有合并单元格
     *
     * @param sheet sheet
     * @return 返回值
     */
    private static boolean hasMerged(Sheet sheet) {
        int numMergedRegions = sheet.getNumMergedRegions();
        if (numMergedRegions > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static Map<Integer, List<Object[]>> readSingleExcelFile(MultipartFile file) throws IOException {
        Map<Integer, List<Object[]>> readContentMap = new HashMap<>();
        String format = sdf.format(new Date());
        Path directoryPath = Paths.get(baseDirectoryPath + "上传文件\\" + format + "\\");
        String fileName = file.getOriginalFilename();
        System.out.println(fileName);
        File dest = new File(directoryPath + "\\" + fileName);
        System.out.println(fileName);
        dest.getParentFile().mkdirs();
        file.transferTo(dest);
        readContentMap = importExcel(new FileInputStream(dest));
        FileUtils.deleteDirectory(new File(baseDirectoryPath + "上传文件\\" + format + "\\"));
        return readContentMap;
    }


    /**
     * 5、读取excel文件内容
     *
     * @param inputStream 输入流
     * @return 返回值
     */
    public static Map<Integer, List<Object[]>> importExcel(InputStream inputStream) {
        //5.1、定义一个集合用来存储Object数据
        Map<Integer, List<Object[]>> listMap = new HashMap<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFFormulaEvaluator formulaEvaluator=workbook.getCreationHelper().createFormulaEvaluator();
            //5.2、创建工作薄
            // Workbook workbook = WorkbookFactory.create(inputStream);
            //5.3、获取工作薄里面sheet的个数
            int sheetNum = workbook.getNumberOfSheets();
            //5.4、遍历每一个sheet
            for (int i = 0; i < sheetNum; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                List<Object[]> list = new ArrayList<>();
                listMap.put(i, list);
                //5.4.1、获取sheet中有数据的行数
                int rows = sheet.getPhysicalNumberOfRows();
                for (int j = 0; j < rows; j++) {
                    //5.4.1.1、过滤掉文件的表头（视文件表头情况而定）
                    if (/*i == 1 ||*/ j == 0) {
                        continue;
                    }
                    //5.4.1.2、获取每一行的数据
                    Row row = sheet.getRow(j);
                    if (row == null) {
                        System.out.println("row is null");
                    } else {
                        //5.4.1.3、得到每一行中有效单元格的数据
                        short cells = row.getLastCellNum();
                        //5.4.1.4、定义一个Object数组用来存储读取单元格的数据
                        Object[] objects = new Object[cells];
                        //5.4.1.5、初始化对象数组的下标
                        int index = 0;
                        //5.4.1.6、遍历每一个有效的单元格数据
                        for (int k = 0; k < cells; k++) {
                            //5.4.1.6.1、获取每一个单元格的数据
                            Cell cell = row.getCell(k, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                            //5.4.1.6.2、判断当前sheet页是否合并有单元格
                            boolean b = hasMerged(sheet);
                            if (b) {
                                //5.4.1.6.2.1、判断当前单元格是不是合并单元格，如果是则输出合并单元格的数据，不是则直接输出
                                List<CellRangeAddress> listCombineCell = getCombineCell(sheet);
                                String combineCell = isCombineCell(listCombineCell, cell, sheet,formulaEvaluator);
                                //5.4.1.6.2.1.2、对单元格的数据进行处理
                                objects[index] = combineCell;
                            } else {
                                String cellValueToString = convertCellValueToString(cell,formulaEvaluator);
                                objects[index] = cellValueToString;
                            }
                            //5.4.1.6.3、下标累加
                            index++;
                        }
                        //5.4.1.7、将对象数组里面的数据添加到list集合中去
                        list.add(objects);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        ;
        //5.5、返回List集合
        return listMap;
    }



    public static void exportExcelFile(List<List<String[]>> needExport, OutputStream outputStream, List<Map<Integer, String[]>>excelListShowMapList) throws IOException {
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
        for (int i = 0; i < needExport.size(); i++) {
            List<String[]> exportContent = needExport.get(i);
            XSSFSheet sheet = workbook.createSheet(exportContent.get(0)[0]);
            for (int l = 1; l < exportContent.size(); l++) {
                XSSFRow row = sheet.createRow(l-1);
                String[] contentArray = exportContent.get(l);
                for (int k = 0; k < contentArray.length; k++) {
                    XSSFCell cell = row.createCell(k);
                    cell.setCellValue(contentArray[k]);
                    if (l == 1) {
                        cell.setCellStyle(headerStyle);
                    } else {
                        cell.setCellStyle(contentStyle);
                    }
                }
                if (l == exportContent.size() - 1) {
                    for (int i1 = 0; i1 < exportContent.get(l).length; i1++) {
                        sheet.autoSizeColumn(i1);
                    }
                    if(excelListShowMapList!=null&&excelListShowMapList.size()>=i){
                        Map<Integer, String[]> excelCellListSelectShowMap = excelListShowMapList.get(i);
                        for (Integer columIndex : excelCellListSelectShowMap.keySet()) {
                            if(exportContent.size()<3){
                                continue;
                            }
                          CellRangeAddressList rangeAddressList=new CellRangeAddressList(1,exportContent.size() - 2,columIndex,columIndex);
                                DataValidationHelper dvHelper = sheet.getDataValidationHelper();
                                DataValidationConstraint dvConstraint = dvHelper.createExplicitListConstraint(excelCellListSelectShowMap.get(columIndex));
                                DataValidation validation = dvHelper.createValidation(dvConstraint, rangeAddressList);
                                validation.setShowErrorBox(true);
                                sheet.addValidationData(validation);
                        }
                    }
                }
            }
        }
        workbook.write(outputStream);
    }
    public static void exportExcelFileToWeb(List<List<String[]>> needExport, HttpServletResponse response,String fileName) throws IOException {
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        ServletOutputStream outputStream = response.getOutputStream();
        exportExcelFile(needExport,outputStream,null) ;
        outputStream.flush();
        outputStream.close();
    }

}
