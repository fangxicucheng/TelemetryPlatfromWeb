package com.fang.database.postgresql.entity;

import com.fang.database.mysql.entity.KeyFrameMq;
import com.fang.database.mysql.entity.TeleFrameCatalogMq;
import com.fang.database.mysql.entity.TeleSatelliteNameMq;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="cg_telemetry_satellite")
public class SatelliteDb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",unique = true,nullable = false)
    private Integer id;
    @Column(name="satellite_name")
    private String satelliteName;
    @Column(name="num")
    private Integer num;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name="satellite_id")
    private List<FrameCatalogDb> frameCatalogDbList;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name="satellite_id")
    private List<KeyFrame> keyFrameList;

    public SatelliteDb(TeleSatelliteNameMq satelliteNameMq) {
        this.satelliteName=satelliteNameMq.getSatelliteName();
        this.num=satelliteNameMq.getNum();
        if(satelliteNameMq.getTeleFrameCatalogMqList()!=null&&satelliteNameMq.getTeleFrameCatalogMqList().size()>0)
        {
            for (TeleFrameCatalogMq teleFrameCatalogMq : satelliteNameMq.getTeleFrameCatalogMqList()) {
                if(frameCatalogDbList==null){
                    this.frameCatalogDbList=new ArrayList<>();
                }
                this.frameCatalogDbList.add(new FrameCatalogDb(teleFrameCatalogMq));
            }
        }
        if(satelliteNameMq.getKeyFrameList()!=null&&satelliteNameMq.getKeyFrameList().size()>0){
            for (KeyFrameMq keyFrameMq : satelliteNameMq.getKeyFrameList()) {
                if(keyFrameList==null){
                    this.keyFrameList=new ArrayList<>();
                }
                this.keyFrameList.add(new KeyFrame(keyFrameMq));
            }
        }
    }
}
