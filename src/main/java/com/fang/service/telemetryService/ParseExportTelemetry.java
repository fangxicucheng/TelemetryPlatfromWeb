package com.fang.service.telemetryService;
import com.fang.config.satellite.paraParser.FrameInfo;
import com.fang.config.satellite.paraParser.ParaParser;
import com.fang.database.postgresql.entity.ReceiveRecord;
import com.fang.service.exportService.exportResult.ExportFrameSingleFrameResult;
import com.fang.service.exportService.exportResult.ExportResult;
import com.fang.service.exportService.needExport.NeedExportFrameInfo;
import com.fang.service.exportService.needExport.NeedExportInfo;
import com.fang.utils.ConfigUtils;
import com.fang.utils.LoadFileUtils;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.util.List;
@Slf4j
public class ParseExportTelemetry {
    public static void exportTelemetry(String satelliteName, List<ReceiveRecord> receiveRecordList, NeedExportInfo needExportInfo, ExportResult exportResult,int frameFlag) {
        ParaParser paraParser = ConfigUtils.getParaParser(satelliteName);

        if (paraParser == null || receiveRecordList == null || receiveRecordList.size() == 0) {
            return;
        }
        parsePrePare(paraParser);
        for (ReceiveRecord receiveRecord : receiveRecordList) {
            try {
                List<byte[]> needExportBytes = LoadFileUtils.readFile(receiveRecord.getFilePath());
                exportSingleFileTelemetry(needExportBytes,paraParser,needExportInfo,exportResult,frameFlag);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        destroyEnd(paraParser);
    }

    public static void exportSingleFileTelemetry(List<byte[]> needExportBytesList, ParaParser paraParser, NeedExportInfo needExportInfo, ExportResult exportResult,int frameFlag) {
        if (needExportBytesList!=null&&needExportBytesList.size()>0) {
            for (byte[] receiveBytes : needExportBytesList) {
                FrameInfo frameInfo = paraParser.parseFrameInfoFromBytes(receiveBytes);
                if(frameInfo!=null&&frameInfo.isValid()&&frameFlag==frameInfo.getFrameFlag()){
                    NeedExportFrameInfo needExportFrame = needExportInfo.getNeedExportFrame(frameInfo.getFrameConfigClass().getFrameName());
                    if(needExportFrame!=null){
                        ExportFrameSingleFrameResult singleFrameResult = paraParser.exportTelemetryFrame(frameInfo, needExportFrame, frameFlag);
                        exportResult.addResult(singleFrameResult);
                    }
                }
            }
        }
    }
    private static void parsePrePare(ParaParser paraParser) {
        paraParser.initThread();
    }

    private static void destroyEnd(ParaParser paraParser) {
        paraParser.destroyThread();
    }
}
