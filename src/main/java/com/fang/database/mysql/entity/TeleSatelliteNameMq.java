package com.fang.database.mysql.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tele_satellite_name")
public class TeleSatelliteNameMq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",unique = true,nullable = false)
    private int id;
    @Column(name="satellite_name")
    private String satelliteName;
    @Column(name="num")
    private Integer num;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name="satellite_name_id")
    private List<TeleFrameCatalogMq> teleFrameCatalogMqList;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name="satellite_id")
    private List<KeyFrameMq> keyFrameList;
}
