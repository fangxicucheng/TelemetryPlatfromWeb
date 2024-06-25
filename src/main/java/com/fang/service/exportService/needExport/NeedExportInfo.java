package com.fang.service.exportService.needExport;

import com.fang.telemetry.TeleCatalogFrameModel;
import com.fang.telemetry.TelemetryFrameModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NeedExportInfo {


    private boolean needExport=false;
    private Map<String, NeedExportFrameInfo> needExportFrameMap;
    public NeedExportInfo(List<TeleCatalogFrameModel> catalogList){
        if(catalogList!=null&&catalogList.size()>0){
            for (TeleCatalogFrameModel catalog : catalogList) {
                this.needExport=true;
                initNeedExportInfo(catalog);
            }
        }

    }

    public void initNeedExportInfo(TeleCatalogFrameModel catalog){

        List<TelemetryFrameModel> frameList = catalog.getFrameList();
        if(frameList!=null&&frameList.size()>0){
            for (TelemetryFrameModel frame : frameList) {
                if(this.needExportFrameMap==null){
                    this.needExportFrameMap=new HashMap<>();
                }
                this.needExportFrameMap.put(frame.getFrameName(),new NeedExportFrameInfo(frame));
            }
        }
    }
    public NeedExportFrameInfo getNeedExportFrame(String frameName){

        if(this.needExportFrameMap!=null&&this.needExportFrameMap.containsKey(frameName)){
            return this.needExportFrameMap.get(frameName);
        }
        return null;
    }

}
