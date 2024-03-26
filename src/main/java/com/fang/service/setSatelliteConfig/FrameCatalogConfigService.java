package com.fang.service.setSatelliteConfig;

import com.fang.database.mysql.entity.TeleSatelliteNameMq;
import com.fang.database.mysql.repository.TeleSatelliteNameMqRepository;
import com.fang.database.postgresql.entity.SatelliteDb;
import com.fang.database.postgresql.repository.FrameCatalogDbRepository;
import com.fang.database.postgresql.repository.SatelliteDbRepository;
import com.fang.telemetry.satelliteConfigModel.TeleFrameCatalogDbModel;
import com.fang.telemetry.satelliteConfigModel.TeleFrameCatalogDbModelInterface;
import com.fang.telemetry.satelliteConfigModel.TeleSatelliteDbModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class FrameCatalogConfigService {

    @Autowired
    private FrameCatalogDbRepository frameCatalogDbRepository;

    public List<TeleFrameCatalogDbModel> getFrameCatalogInfo(Integer satelliteId){

       List<TeleFrameCatalogDbModel>result=new ArrayList<>();
        List<TeleFrameCatalogDbModelInterface> searchResult = this.frameCatalogDbRepository.getFrameCatalogDbModelListByBId(satelliteId);

        if(searchResult!=null&&searchResult.size()>0){
            for (TeleFrameCatalogDbModelInterface teleFrameCatalogDbModelInterface : searchResult) {
                result.add(new TeleFrameCatalogDbModel(teleFrameCatalogDbModelInterface));

            }
        }
        return result;

    }
    public List<TeleFrameCatalogDbModel> updateFrameCatalogById(Integer satelliteId,TeleFrameCatalogDbModel teleFrameCatalogDbModel){
        this.frameCatalogDbRepository.updateFrameCatalogById(teleFrameCatalogDbModel.getId(),teleFrameCatalogDbModel.getCatalogName(),teleFrameCatalogDbModel.getCatalogCode(),teleFrameCatalogDbModel.getNum());
        return getFrameCatalogInfo(satelliteId);
    }

    public List<TeleFrameCatalogDbModel> deleteFrameCatlogById(Integer satelliteId,int id){
        this.frameCatalogDbRepository.deleteById(id);
        return getFrameCatalogInfo(satelliteId);
    }


}
