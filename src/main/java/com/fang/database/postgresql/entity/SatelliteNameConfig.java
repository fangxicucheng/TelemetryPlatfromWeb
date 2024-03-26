package com.fang.database.postgresql.entity;

import com.fang.database.mysql.entity.TeleSatelliteNameConfigMq;
import com.fang.database.mysql.entity.TeleSatelliteNameMq;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="cg_telemetry_satellite_name_config")
public class SatelliteNameConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;
    @Column(name="satellite_name")
    private String satelliteName;
    @Column(name="satellite_name_para_code")
    private String satelliteNameParaCode;
    @Column(name="series_name_para_code")
    private String seriesNameParaCode;
    @Column(name="series_name")
    private String seriesName;
    @Column(name="frame_name")
    private String frameName;

    public SatelliteNameConfig(TeleSatelliteNameConfigMq satelliteNameConfigMq) {
        this.satelliteName=satelliteNameConfigMq.getSatelliteName();
        this.satelliteNameParaCode=satelliteNameConfigMq.getSatelliteNameParaCode();
        this.seriesNameParaCode=satelliteNameConfigMq.getSeriesNameParaCode();
        this.seriesName=satelliteNameConfigMq.getSeriesName();
        this.frameName=satelliteNameConfigMq.getFrameName();
    }
}
