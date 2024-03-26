package com.fang.database.mysql.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tele_countpara")
public class TeleCountparaMq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;
    @Column(name="satellite_name")
    private String satelliteName;
    @Column(name="start_time")
    private Date datetime;
    @Column(name="paracode")
    private String paracode;
    @Column(name="paraname")
    private String paraname;
    @Column(name="paravalue")
    private Double paravalue;

}
