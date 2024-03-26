package com.fang.database.mysql.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="tele_plan")

public class TelePlanMq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",unique = true,nullable = false)
    private int Id;
    @Column(name="orbit")
    private String orbit;
    @Column(name="satelliteName")
    private String satelliteName;
    @Column(name="stationName")
    private String stationName;
    @Column(name="startTime")
    private Date startTime;
    @Column(name="endTime")
    private Date endTime;
    @Column(name="state")
    private String state;
    @Column(name="taskType")
    private  String taskType;
    @Column(name="command")
    private String  command;
}
