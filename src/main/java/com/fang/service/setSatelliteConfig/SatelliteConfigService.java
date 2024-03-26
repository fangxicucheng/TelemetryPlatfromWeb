package com.fang.service.setSatelliteConfig;

import com.fang.database.mysql.entity.TeleSatelliteNameMq;
import com.fang.database.mysql.repository.TeleSatelliteNameMqRepository;
import com.fang.database.postgresql.entity.SatelliteDb;
import com.fang.database.postgresql.repository.SatelliteDbRepository;
import com.fang.telemetry.satelliteConfigModel.TeleSatelliteDbModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SatelliteConfigService {

    @Autowired
    private TeleSatelliteNameMqRepository satelliteNameMqRepository;
    @Autowired
    private SatelliteDbRepository satelliteDbRepository;

    public void copySatelliteData() {
        List<TeleSatelliteNameMq> satelliteMqList = this.satelliteNameMqRepository.findAll();
        List<SatelliteDb> satelliteDbList = new ArrayList<>();
        for (TeleSatelliteNameMq teleSatelliteNameMq : satelliteMqList) {
            SatelliteDb satelliteDb = new SatelliteDb(teleSatelliteNameMq);
            satelliteDbList.add(satelliteDb);
        }
        this.satelliteDbRepository.saveAll(satelliteDbList);

    }

    public List<TeleSatelliteDbModel> getTeleSatelliteDbModelList() {
        return this.satelliteDbRepository.querySatelliteName();
    }
    public List<TeleSatelliteDbModel>updateTeleSatelliteDbInfo(TeleSatelliteDbModel satelliteDbModel){
        this.satelliteDbRepository.updateSatelliteDbInfo(satelliteDbModel.getId(),satelliteDbModel.getSatelliteName());
        return getTeleSatelliteDbModelList();

    }

    public List<TeleSatelliteDbModel>deleteTeleSatelliteDbInfoById(int id){
        this.satelliteDbRepository.deleteById(id);
        return getTeleSatelliteDbModelList();
    }
}
