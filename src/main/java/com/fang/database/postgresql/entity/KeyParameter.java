package com.fang.database.postgresql.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "cg_telemetry_key_parameter")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyParameter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "satellite_id")
    private String satelliteId;

    @Column(name = "key_parameter")
    private String keyParameter;
}
