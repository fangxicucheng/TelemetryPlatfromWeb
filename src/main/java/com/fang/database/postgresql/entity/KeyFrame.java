package com.fang.database.postgresql.entity;

import com.fang.database.mysql.entity.KeyFrameMq;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="cg_telemetry_key_frame")
public class KeyFrame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",unique = true,nullable = false)
    private int id;
    @Column(name="frame_name")
    private String frameName;
    @Column(name="frame_code")
    private Integer frameCode;
    @Column(name="para_codes",length = 8192)
    private String paraCodes;
    @Column(name="num")
    private Integer num;

    public KeyFrame(KeyFrameMq keyFrameMq) {
        this.frameName= keyFrameMq.getFrameName();
        this.frameCode= keyFrameMq.getFrameCode();
        this.paraCodes= keyFrameMq.getParaCodes();
        this.num= keyFrameMq.getNum();
    }
}
