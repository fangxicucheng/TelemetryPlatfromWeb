package com.fang.database.postgresql.entity;

import com.fang.database.mysql.entity.TeleExceptionMq;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="cg_telemetry_exception_file")
public class ExceptionFile  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;
    @Column(name="satellite_name")
    private String satelliteName;
    @Column(name="start_time")
    private Date startTime;
    @Column(name="exp_count")
    private Integer expCount;
    @Column(name="path")
    private String path;

    public ExceptionFile(TeleExceptionMq exceptionMq) {
        this.satelliteName=exceptionMq.getSatilleteName();
        this.startTime=exceptionMq.getStartTime();
        this.expCount=exceptionMq.getExpCount();
        this.path=exceptionMq.getPath();
    }
}
