package com.fang.service.setSatelliteConfig;

import com.fang.database.postgresql.repository.FrameDbRepository;
import com.fang.telemetry.satelliteConfigModel.TeleFrameDbModel;
import com.fang.telemetry.satelliteConfigModel.TeleFrameDbModelInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FrameConfigService {
    @Autowired
    private FrameDbRepository frameDbRepository;

    public List<TeleFrameDbModel> getFrameDbModelList(Integer catalogId){

        List<TeleFrameDbModel>result=new ArrayList<>();
        List<TeleFrameDbModelInterface> searchResult = this.frameDbRepository.getFrameByCatalogId(catalogId);
        if(searchResult!=null&&searchResult.size()>0){
            for (TeleFrameDbModelInterface teleFrameDbModelInterface : searchResult) {
                result.add(new TeleFrameDbModel(teleFrameDbModelInterface));
            }

        }
        return result;



    }

    public void deleteFrmeById(Integer frameId){
        this.frameDbRepository.deleteById(frameId);

    }


    public void updateFrameByFrameInfo(TeleFrameDbModel teleFrameDbModel){
        frameDbRepository.updateFrameByFrameInfo(teleFrameDbModel.getFrameCode(),teleFrameDbModel.getFrameName(),teleFrameDbModel.getReuseChannel(),teleFrameDbModel.getNum(),teleFrameDbModel.getId());
    }
}
