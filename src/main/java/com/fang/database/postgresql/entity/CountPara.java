package com.fang.database.postgresql.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="cg_telemetry_count_para")
public class CountPara  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;
    @Column(name="satellite_name")
    private String satelliteName;
    @Column(name="start_time")
    private Date dateTime;
    @Column(name="para_code")
    private String paraCode;
    @Column(name="para_name")
    private String paraName;
    @Column(name="para_value")
    private Double paraValue;
}
