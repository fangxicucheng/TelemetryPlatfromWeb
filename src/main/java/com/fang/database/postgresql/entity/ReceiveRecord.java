package com.fang.database.postgresql.entity;

import com.fang.database.mysql.entity.TeleReceiveRecordMq;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cg_telemetry_receive_record")
public class ReceiveRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;
    @Column(name = "satellite_name")
    private String satelliteName;
    @Column(name = "start_time")
    private Date startTime;
    @Column(name = "end_time")
    private Date endTime;
    @Column(name = "file_path")
    private String filePath;
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name="exp_id")
    private ExceptionFile exception;

    public ReceiveRecord(TeleReceiveRecordMq receiveRecordMq) {
        this.satelliteName= receiveRecordMq.getSatelliteName();;
        this.startTime=receiveRecordMq.getStartTime();
        this.endTime=receiveRecordMq.getEndTime();
        this.filePath=receiveRecordMq.getFilePath();
        if(exception!=null){
            this.exception=new ExceptionFile(receiveRecordMq.getTeleException());
        }
    }
}
