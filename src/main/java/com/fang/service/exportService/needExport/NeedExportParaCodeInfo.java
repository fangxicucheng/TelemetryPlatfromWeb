package com.fang.service.exportService.needExport;

import com.fang.telemetry.TelemetryParameterModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NeedExportParaCodeInfo {
    private String paraCode;
    private String paraName;
    public NeedExportParaCodeInfo(TelemetryParameterModel param){
        this.paraCode=param.getParaCode();
        this.paraName=param.getParaName();
    }


}
