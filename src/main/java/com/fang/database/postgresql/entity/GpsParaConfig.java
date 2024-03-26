package com.fang.database.postgresql.entity;

import com.fang.database.mysql.entity.TeleGpsParaConfigMq;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="cg_telemetry_gps_para_config")
public class GpsParaConfig  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false,unique = true)
    private int id;
    @Column(name="satellite_name")
    private String satelliteName;
    @Column(name="time_para_code")
    private String timeParaCode;
    @Column(name="x_location_para_code")
    private String xLocationParaCode;
    @Column(name="y_location_para_code")
    private String yLocationParaCode;
    @Column(name="z_location_para_code")
    private String zLocationParaCode;
    @Column(name="x_speed_para_code")
    private String xSpeedParaCode;
    @Column(name="y_speed_para_code")
    private String ySpeedParaCode;
    @Column(name="z_speed_para_code")
    private String zSpeedParaCode;
    @Column(name="frame_name")
    private String frameName;

    public GpsParaConfig(TeleGpsParaConfigMq gpsParaConfigMq) {
        this.satelliteName=gpsParaConfigMq.getSatelliteName();
        this.timeParaCode=gpsParaConfigMq.getTimeParaCode();
        this.xLocationParaCode=gpsParaConfigMq.getXLocationParaCode();
        this.yLocationParaCode=gpsParaConfigMq.getYLocationParaCode();
        this.zLocationParaCode=gpsParaConfigMq.getZLocationParaCode();
        this.xSpeedParaCode=gpsParaConfigMq.getXSpeedParaCode();
        this.ySpeedParaCode=gpsParaConfigMq.getYSpeedParaCode();
        this.zSpeedParaCode=gpsParaConfigMq.getZSpeedParaCode();
        this.frameName=gpsParaConfigMq.getFrameName();
    }
}
