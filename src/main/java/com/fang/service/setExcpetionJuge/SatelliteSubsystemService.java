package com.fang.service.setExcpetionJuge;

import com.fang.database.mysql.entity.TeleSubsystemMq;
import com.fang.database.mysql.repository.TeleSubsystemMqRepository;
import com.fang.database.postgresql.entity.SatelliteSubsystem;
import com.fang.database.postgresql.repository.SatelliteSubsystemRepository;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SatelliteSubsystemService {
    @Autowired
    private TeleSubsystemMqRepository teleSubsystemMqRepository;
    @Autowired
    private SatelliteSubsystemRepository satelliteSubsystemRepository;

    public void copySatelliteSubsystemFromMysql() {
        List<TeleSubsystemMq> teleSubsystemMqList = teleSubsystemMqRepository.findAll();
        for (TeleSubsystemMq teleSubsystemMq : teleSubsystemMqList) {
            this.satelliteSubsystemRepository.save(new SatelliteSubsystem(teleSubsystemMq));
        }
    }


}
