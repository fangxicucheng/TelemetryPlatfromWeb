package com.fang.database.postgresql.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "cg_telemetry_plan")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TelemetryPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "telemetry_plan_id")
    private String telemetryPlanId;

    @Column(name = "satellite_id")
    private String satelliteId;

    @Column(name = "station_id")
    private String stationId;

    @Column(name="antenna_id")
    private String antennaId;


    @Transient
    private int num;

    @Transient
    private String label;

    @Transient
    private String satelliteName;

    @Transient
    private String stationName;
    @Transient
    private String antennaName;



    @Column(name = "orbit")
    private int orbit;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "status")
    private String status;

    @Column(name = "plan_type")
    private String planType;


    @Column(name="command_count")
    private Integer commandCount;

    @Column(name="max_elevation",columnDefinition="double default -1.00")
    private Double maxElevation;

}
