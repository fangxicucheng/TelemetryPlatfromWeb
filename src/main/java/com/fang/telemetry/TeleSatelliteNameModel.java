package com.fang.telemetry;

import com.fang.database.postgresql.entity.FrameCatalogDb;
import com.fang.database.postgresql.entity.KeyFrame;
import com.fang.database.postgresql.entity.SatelliteDb;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.WhereJoinTable;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeleSatelliteNameModel {
    private String SatelliteName;
    private List<KeyFrame> keyFrameList;

    private List<TeleCatalogFrameModel> teleCatalogFrameModelList;
    public TeleSatelliteNameModel(SatelliteDb satelliteDb){

     this.keyFrameList=   satelliteDb.getKeyFrameList();

        List<FrameCatalogDb> frameCatalogDbList = satelliteDb.getFrameCatalogDbList();
        if(frameCatalogDbList!=null&&frameCatalogDbList.size()>0){
            for (FrameCatalogDb frameCatalogDb : frameCatalogDbList) {

                if(teleCatalogFrameModelList==null){
                    teleCatalogFrameModelList=new ArrayList<>();
                }
                teleCatalogFrameModelList.add(new TeleCatalogFrameModel(frameCatalogDb));

            }
        }
    }

}
