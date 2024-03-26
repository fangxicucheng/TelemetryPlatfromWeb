package com.fang.database.postgresql.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="cg_telemetry_command_count")
public class CommandCount  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;
    @Column(name="satellite_name")
    private String satelliteName;
    @Column(name="frame_name")
    private String frameName;
    @Column(name="total_command_para_code")
    private String totalCommandParaCode;
    @Column(name="total_command_para_name")
    private String totalCommandParaName;
    @Column(name="total_command_count")
    private Integer totalCommandCount;
    @Column(name="error_command_para_code")
    private String errorCommandParaCode;
    @Column(name="error_command_para_name")
    private String errorCommandParaName;
    @Column(name="error_command_count")
    private Integer errorCommandCount;


}
