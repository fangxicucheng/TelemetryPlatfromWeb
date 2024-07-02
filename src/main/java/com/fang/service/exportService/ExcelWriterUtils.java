package com.fang.service.exportService;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;

import java.util.List;

public class ExcelWriterUtils {
    private ExcelWriter writer;
    private WriteSheet writeSheet;

    public ExcelWriterUtils(String filePath, String frameName) {
        this.writer = EasyExcel.write(filePath).build();
        this.writeSheet = EasyExcel.writerSheet(frameName).build();


    }

    public void write(List<List<Object>> writeDataList) {
        this.writer.write(writeDataList, writeSheet);
    }
    public void finish(){
        writer.finish();
    }

}
