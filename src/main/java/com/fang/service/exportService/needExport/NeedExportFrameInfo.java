package com.fang.service.exportService.needExport;

import com.fang.telemetry.TelemetryFrameModel;
import com.fang.telemetry.TelemetryParameterModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NeedExportFrameInfo {
    private String frameName;
    private Map<String,NeedExportParaCodeInfo>needExportParaCodeInfoMap;
    private List<NeedExportParaCodeInfo>needExportParaCodeInfoList;

    public NeedExportFrameInfo(TelemetryFrameModel frame){
        this.frameName=frame.getFrameName();
        List<TelemetryParameterModel> parameterList = frame.getParameterList();
        if(parameterList!=null&&parameterList.size()>0){
            for (TelemetryParameterModel param : parameterList) {
                if(this.needExportParaCodeInfoList==null){
                    this.needExportParaCodeInfoList=new ArrayList<>();
                }
                if(this.needExportParaCodeInfoMap==null){
                    this.needExportParaCodeInfoMap=new HashMap<>();
                }
                NeedExportParaCodeInfo needExportParaCodeInfo = new NeedExportParaCodeInfo(param);
                this.needExportParaCodeInfoMap.put(param.getParaCode(),needExportParaCodeInfo);
                needExportParaCodeInfoList.add(needExportParaCodeInfo);

            }
        }
    }

    public boolean checkParamNeedExport(String paraCode){
        if(this.needExportParaCodeInfoMap!=null&&this.needExportParaCodeInfoMap.containsKey(paraCode)){
            return true;
        }
        return false;
    }
}
