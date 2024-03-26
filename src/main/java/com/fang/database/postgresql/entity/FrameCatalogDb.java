package com.fang.database.postgresql.entity;

import com.fang.database.mysql.entity.TeleFrameCatalogMq;
import com.fang.database.mysql.entity.TeleFrameMq;
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
@Table(name="cg_telemetry_frame_catalog_config")
public class FrameCatalogDb  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",unique = true,nullable = false)
    private Integer id;
    @Column(name="catalog_name")
    private String catalogName;
    @Column(name="num")
    private Integer num;
    @Column(name="catalog_code")
    private Integer catalogCode;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name="catalog_id")
    private List<FrameDb> frameDbList;
    public FrameCatalogDb(TeleFrameCatalogMq frameCatalogMq) {
        this.catalogName=frameCatalogMq.getCatalogName();
        this.num=frameCatalogMq.getNum();
        this.catalogCode=frameCatalogMq.getCatalogCode();
        int index=1;
        if(frameCatalogMq.getFrameMqList()!=null&&frameCatalogMq.getFrameMqList().size()>0){
            for (TeleFrameMq teleFrameMq : frameCatalogMq.getFrameMqList()) {
                if(this.frameDbList==null){
                    this.frameDbList=new ArrayList<>();
                }
                FrameDb frameDb = new FrameDb(teleFrameMq);
                frameDb.setNum(index);
                index++;
                this.frameDbList.add(frameDb);
            }
        }
    }
}
