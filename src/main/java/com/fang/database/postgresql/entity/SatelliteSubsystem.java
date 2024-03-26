package com.fang.database.postgresql.entity;

import com.fang.database.mysql.entity.TeleSubsystemMq;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="cg_telemetry_satellite_subsystem")
public class SatelliteSubsystem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;
    @Column(name="satellite_name")
    private String satelliteName;
    @Column(name="subsystem_name")
    private String subsystemName;
    @Column(name="subsystem_charger_contact")
    private String subsystemChargerContact;

    public SatelliteSubsystem(TeleSubsystemMq subsystemMq) {
        this.satelliteName=subsystemMq.getSatelliteName();
        this.subsystemName=subsystemMq.getSubsystemName();
        this.subsystemChargerContact=subsystemMq.getSubsystemChargerContact();

    }
}
