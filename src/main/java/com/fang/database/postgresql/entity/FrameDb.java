package com.fang.database.postgresql.entity;

import com.fang.database.mysql.entity.TeleFrameMq;
import com.fang.database.mysql.entity.TeleParaConfigLineMq;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="cg_telemetry_frame_config")
public class FrameDb  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false,unique = true)
    private Integer id;
    @Column(name="frame_name")
    private String frameName;
    @Column(name="num")
    private Integer num;
    @Column(name="frame_code")
    private Integer frameCode;
    @Column(name="reuse_channel")
    private Integer reuseChannel;
    @OneToMany(cascade=CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name="frame_id")
    private List<ParaConfigLineDb> paraConfigLineDbList;

    public FrameDb(TeleFrameMq frameMq) {
        this.frameName=frameMq.getFrameName();
        this.frameCode=frameMq.getFrameCode();
        this.reuseChannel=Integer.parseInt(frameMq.getReuseChannel());
        int index=1;
        if(frameMq.getParaConfigLineList()!=null&&frameMq.getParaConfigLineList().size()>0){
            for (TeleParaConfigLineMq teleParaConfigLineMq : frameMq.getParaConfigLineList())
            {
                if(paraConfigLineDbList==null){
                    this.paraConfigLineDbList=new ArrayList<>();
                }
                ParaConfigLineDb paraConfigLineDb = new ParaConfigLineDb(teleParaConfigLineMq);
                paraConfigLineDb.setNum(index);
                index++;
                this.paraConfigLineDbList.add(paraConfigLineDb);
            }
        }
    }
}
