package com.fang.database.mysql.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tele_satellite_name_config")
public class TeleSatelliteNameConfigMq {
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
}
