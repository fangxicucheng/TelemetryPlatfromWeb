package com.fang.utils;

import com.fang.config.satellite.configStruct.SatelliteConfigClass;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ConfigUtils {

    private static Map<String, String> satelliteNameMap = new HashMap<>();
    private static Map<String, String> satelliteIdMap = new HashMap<>();
    private static Map<String, byte[]> midMap = new HashMap<>();
    private static Map<String, String> icCardMap = new HashMap<>();

    public static void setSatelliteName(String satelliteName, String satelliteId) {
        if (!satelliteNameMap.containsKey(satelliteName)) {
            satelliteNameMap.put(satelliteName, satelliteId);
        }
        if (!satelliteIdMap.containsKey(satelliteId)) {
            satelliteIdMap.put(satelliteId, satelliteName);
        }
    }

    public static void setIcCard(String satelliteName, String icCard) {
        icCardMap.put(satelliteName, icCard);
    }

    public static void setMid(String satelliteName, byte[] midBytes) {
        if (!midMap.containsKey(satelliteName)) {
            midMap.put(satelliteName, midBytes);
        }
    }

    public static String getSatelliteNameByMid(byte midByte1, byte midByte2) {
        String satelliteName = null;
        for (String satelliteNameKey : midMap.keySet()) {
            byte[] bytes = midMap.get(satelliteNameKey);
            if (bytes[0] == midByte1 && bytes[1] == midByte2) {
                satelliteName = satelliteNameKey;
                break;
            }
        }
        return satelliteName;
    }

    public static String getSatelliteIdBySatelliteName(String satelliteName) {
        String satelliteId = null;
        if (satelliteNameMap.containsKey(satelliteName)) {
            satelliteId = satelliteNameMap.get(satelliteName);
        }
        return satelliteId;
    }
    public static String getSatelliteNameByIcCard(String icCard){
        String satelliteName=null;
        if(icCardMap.containsKey(icCard)){
            satelliteName=icCardMap.get(icCard);
        }
        return satelliteName;
    }

    public static String getSatelliteNameBySatelliteId(String satelliteId) {
        String satelliteName = null;
        if (satelliteIdMap.containsKey(satelliteId)) {

            satelliteName = satelliteIdMap.get(satelliteId);

        }
        return satelliteName;

    }


}
