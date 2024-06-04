package com.fang.database.postgresql.entity;

import com.fang.database.mysql.entity.TeleReceiveRecordMq;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "start_time")
    private Date startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "end_time")
    private Date endTime;
    @Column(name = "file_path")
    private String filePath;
    @Column(name = "error_rate")
    private Double errorRate;
    @Column(name = "frame_num")
    private Integer frameNum;
    @Column(name = "local_frame_num")
    private Integer localFrameNum;
    @Column(name="station_name")
    private String stationName;

    public ReceiveRecord(TeleReceiveRecordMq receiveRecordMq) {
        this.satelliteName= receiveRecordMq.getSatelliteName()!=null?receiveRecordMq.getSatelliteName().replaceAll("_北斗",""):receiveRecordMq.getSatelliteName();;
        this.startTime=receiveRecordMq.getStartTime();
        this.endTime=receiveRecordMq.getEndTime();
        this.filePath=receiveRecordMq.getFilePath();
        this.errorRate=receiveRecordMq.getErrorRate();
        this.frameNum=receiveRecordMq.getFrameNum();
        this.localFrameNum= receiveRecordMq.getLocalFrameNum();
        this.stationName = this.filePath.split("_")[1];
    }
}
