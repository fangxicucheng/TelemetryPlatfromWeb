package com.fang.database.mysql.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="tele_gps_para_config")
public class TeleGpsParaConfigMq {
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
}
