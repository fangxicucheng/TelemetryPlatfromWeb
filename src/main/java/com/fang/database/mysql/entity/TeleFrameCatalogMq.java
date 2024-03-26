package com.fang.database.mysql.entity;

import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tele_frame_catalog")
public class TeleFrameCatalogMq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",unique = true,nullable = false)
    private int id;
    @Column(name="catagory")
    private String catalogName;
    @Column(name="num")
    private Integer num;
    @Column(name="frame_catalog_code")
    private Integer catalogCode;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name="catalog_id")
    private List<TeleFrameMq> frameMqList;
}
