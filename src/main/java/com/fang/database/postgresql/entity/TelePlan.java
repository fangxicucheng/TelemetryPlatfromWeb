package com.fang.database.postgresql.entity;

import com.fang.database.mysql.entity.TelePlanMq;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cg_telemetry_tele_plan")
public class TelePlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int Id;
    @Column(name = "orbit")
    private String orbit;
    @Column(name = "satelliteName")
    private String satelliteName;
    @Column(name = "stationName")
    private String stationName;
    @Column(name = "startTime")
    private Date startTime;
    @Column(name = "endTime")
    private Date endTime;
    @Column(name = "state")
    private String state;
    @Column(name = "taskType")
    private String taskType;
    @Column(name = "command")
    private String command;

    public TelePlan(TelePlanMq telePlanMq) {
        this.orbit = telePlanMq.getOrbit();
        this.satelliteName=telePlanMq.getSatelliteName();
        this.stationName=telePlanMq.getStationName();
        this.startTime=telePlanMq.getStartTime();
        this.endTime=telePlanMq.getEndTime();
        this.state=telePlanMq.getState();
        this.taskType=telePlanMq.getTaskType();
        this.command=telePlanMq.getCommand();
    }
}
