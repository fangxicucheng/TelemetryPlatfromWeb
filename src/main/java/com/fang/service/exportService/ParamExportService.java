package com.fang.service.exportService;

import com.fang.database.postgresql.entity.ReceiveRecord;
import com.fang.service.exportService.model.ExportRequestInfo;
import com.fang.service.saveService.ReceiveRecordService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class ParamExportService {

    @Autowired
    private ReceiveRecordService receiveRecordService;

    public List<ReceiveRecord> getReceiveRecordList(ExportRequestInfo exportRequestInfo){
        return receiveRecordService.getReceiveRecordList(exportRequestInfo.getSatelliteName().replaceAll("_北斗",""),exportRequestInfo.getStationNameList(),exportRequestInfo.getStartTime(),exportRequestInfo.getEndTime());
    }

    public void exportParam(int frameFlag,  ExportRequestInfo exportRequestInfo, HttpServletResponse response){
        List<ReceiveRecord> receiveRecordList = getReceiveRecordList(exportRequestInfo);
        if(receiveRecordList==null||receiveRecordList.size()==0){

        }
    }


}
