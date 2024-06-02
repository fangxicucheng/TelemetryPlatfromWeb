package com.fang.service.saveService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiveRecordRequestInfo {
    private int pageNum;
    private int pageSize;
    private List<String> satelliteNameList;
    private List<String>stationNameList;
    private Date startTime;
    private Date endTime;
}
