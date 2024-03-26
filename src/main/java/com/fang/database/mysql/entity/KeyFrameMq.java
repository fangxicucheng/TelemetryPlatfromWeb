package com.fang.database.mysql.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="key_frame")
public class KeyFrameMq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",unique = true,nullable = false)
    private int id;
    @Column(name="frame_name")
    private String frameName;
    @Column(name="frame_code")
    private Integer frameCode;
    @Column(name="para_codes",length = 8190)
    private String paraCodes;
    @Column(name="num")
    private Integer num;



}
