package com.fang.database.postgresql.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@Table(name = "cg_telemetry_exception_parameter_info")
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionParameterInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;
    @Column(name="teleplan_id")
    private String teleplanId;
    @Column(name="para_code")
    private String paraCode;
    @Column(name="para_name")
    private String paraName;
    @Column(name="frame_name")
    private String frameName;
    @Column(name="para_value")
    private String paraValue;
    @Column(name="satellite_time")
    private String satelliteTime;
    @Column(name="threshold")
    private String threshold;
    @ManyToOne
    @JoinColumn(name="replay_id")
    private TelemetryReplay telemetryReplay;

    @Transient
    private String subsystemName;

    @Transient
    private String subssystemChargerContact;
}
