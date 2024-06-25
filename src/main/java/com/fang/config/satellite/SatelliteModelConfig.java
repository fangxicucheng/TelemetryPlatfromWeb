package com.fang.config.satellite;

import com.fang.database.postgresql.entity.SatelliteDb;
import com.fang.telemetry.TeleSatelliteNameModel;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class SatelliteModelConfig {

    private static Map<String, TeleSatelliteNameModel> satelliteNameModelMap;

    public static void setTeleSatelliteNameModel(SatelliteDb satelliteDb) {
        if (satelliteNameModelMap == null) {
            satelliteNameModelMap = new HashMap<>();
        }
        satelliteNameModelMap.put(satelliteDb.getSatelliteName(), new TeleSatelliteNameModel(satelliteDb));
    }

    public static TeleSatelliteNameModel getSatelliteNameModel(String satelliteName) {

        if (satelliteNameModelMap != null) {
            if (satelliteNameModelMap.containsKey(satelliteName)) {
                return satelliteNameModelMap.get(satelliteName);
            }
        }
        return null;
    }
}
