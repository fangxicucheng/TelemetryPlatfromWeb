package com.fang.service.exportService.model;

import com.fang.telemetry.TeleCatalogFrameModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExportRequestInfo {
    private String satelliteName;
    private List<String>stationNameList;
    private Date startTime;
    private Date endTime;
    private List<TeleCatalogFrameModel> catalogList;

}
