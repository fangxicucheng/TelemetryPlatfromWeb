package com.fang.database.postgresql.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="cg_stationIfo")
public class StationInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;
    @Column(name="station_name")
    private String stationName;
    @Column(name="station_id")
    private String stationId;
    @Column(name="wave_info")
    private String waveInfo;
}
