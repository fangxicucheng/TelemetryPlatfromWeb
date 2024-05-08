package com.fang.telemetry.satelliteConfigModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeleSatelliteDbModel  {
    private Integer id;
    private String satelliteName;
    private String satelliteId;
    private String satelliteBytesStr;
    private String bdICCardsStr;
}
