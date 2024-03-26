package com.fang.database.mysql.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="tele_frame")
public class TeleFrameMq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false,unique = true)
    private int id;
    @Column(name="frame_name")
    private String frameName;
    @Column(name="num")
    private Integer num;
    @Column(name="frame_code")
    private Integer frameCode;
    @Column(name="reuse_channel")
    private String reuseChannel;
    @OneToMany(cascade=CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name="frame_id")
    private List<TeleParaConfigLineMq> paraConfigLineList;

}
