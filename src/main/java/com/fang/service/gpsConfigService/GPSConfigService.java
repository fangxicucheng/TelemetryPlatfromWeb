package com.fang.service.gpsConfigService;

import com.fang.database.postgresql.repository.GpsParaConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GPSConfigService {
    @Autowired
    private GpsParaConfigRepository gpsParaConfigDao;

}
