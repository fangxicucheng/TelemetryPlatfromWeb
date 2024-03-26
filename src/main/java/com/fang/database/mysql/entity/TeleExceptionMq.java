package com.fang.database.mysql.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="tele_exception")
public class TeleExceptionMq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;
    @Column(name="satillete_name")
    private String satilleteName;
    @Column(name="start_time")
    private Date startTime;
    @Column(name="exp_count")
    private Integer expCount;
    @Column(name="path")
    private String path;

}
