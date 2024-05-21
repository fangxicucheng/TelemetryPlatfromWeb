package com.fang.database.postgresql.entity;

import com.fang.database.mysql.entity.TeleRestartTimeMq;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="restart_time_record")
public class RestartTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    @Column(name="satellite_name")
    private String satelliteName;
    @Column(name="restart_time")
    private Date restartTime;
    @CreatedDate
    @Column(nullable = false, updatable = false ,name="create_time")
    private Date createTime;
    @LastModifiedDate
    @Column(name="update_time")
    private Date updateTime;

    public RestartTime(TeleRestartTimeMq teleRestartTimeMq) {
        this.satelliteName=teleRestartTimeMq.getSatelliteName();
        this.restartTime=teleRestartTimeMq.getRestartTime();
    }
}
