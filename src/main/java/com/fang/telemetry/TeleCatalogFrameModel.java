package com.fang.telemetry;

import com.fang.database.postgresql.entity.FrameCatalogDb;
import com.fang.database.postgresql.entity.FrameDb;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeleCatalogFrameModel {
    private String catalogName;
    private List<TelemetryFrameModel>frameList;
    public TeleCatalogFrameModel(FrameCatalogDb frameCatalogDb){

        this.catalogName=frameCatalogDb.getCatalogName();
        if(frameCatalogDb.getFrameDbList()!=null&&frameCatalogDb.getFrameDbList().size()>0){
            for (FrameDb frameDb : frameCatalogDb.getFrameDbList()) {
                if(frameList==null){
                    frameList=new ArrayList<>();
                }
                frameList.add(new TelemetryFrameModel(frameDb));
            }
        }

    }
}
