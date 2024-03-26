package com.fang.database.postgresql.entity;

import com.fang.telemetry.TelemetryExceptionPara;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "cg_telemetry_replay")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class TelemetryReplay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "telemetry_plan_id")
    private String telemetryPlanId;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "replay_file")
    private String replayFile;

    @Column(name = "frame_num")
    private int frameNum;

    @Column(name = "local_frame_num", columnDefinition = "int default 0")
    private Integer localFrameNum;
    @Column(name = "error_code_num", columnDefinition = "int default 0")
    private Integer errorCodeNum;
    @Column(name="antenna_id",columnDefinition = "Varchar(255) not null default ''")
    private String antennaId;
    @Column(name="exception_status")
    private String exceptionStatus;
    @Column(name="restart_time")
    private String restartTime;
    @Column(name="need_insert_command")
    private Integer needInsertCommand;
    @Column(name="has_insert_command")
    private Integer hasInsertCommand;
    @Column(name="plan_type")
    private String planType;
    @Transient
    private String antennaName;
    @Transient
    private boolean isRestart;

    @Column(name="has_receive",columnDefinition = "boolean default true")
    private Boolean hasReceive;

    @Column(name="max_elevation" ,columnDefinition="double default -1.00")
    private Double maxElevation;

    public TelemetryReplay() {
        this.isRestart=false;
    }


    @OneToMany(cascade=CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name="replay_id")
    private List<ExceptionParameterInfo> exceptionParameterInfoList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTelemetryPlanId() {
        return telemetryPlanId;
    }

    public void setTelemetryPlanId(String telemetryPlanId) {
        this.telemetryPlanId = telemetryPlanId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getReplayFile() {
        return replayFile;
    }

    public void setReplayFile(String replayFile) {
        this.replayFile = replayFile;
    }

    public int getFrameNum() {
        return frameNum;
    }

    public void setFrameNum(int frameNum) {
        this.frameNum = frameNum;
    }

    public Integer getLocalFrameNum() {
        return localFrameNum;
    }

    public void setLocalFrameNum(Integer localFrameNum) {
        this.localFrameNum = localFrameNum;
    }

    public Integer getErrorCodeNum() {
        return errorCodeNum;
    }

    public void setErrorCodeNum(Integer errorCodeNum) {
        this.errorCodeNum = errorCodeNum;
    }

    public String getAntennaId() {
        return antennaId;
    }

    public void setAntennaId(String antennaId) {
        this.antennaId = antennaId;
    }

    public String getExceptionStatus() {
        return exceptionStatus;
    }

    public void setExceptionStatus(String exceptionStatus) {
        this.exceptionStatus = exceptionStatus;
    }

    public String getRestartTime() {
        return restartTime;
    }

    public void setRestartTime(String restartTime) {
        this.restartTime = restartTime;
    }

    public Integer getNeedInsertCommand() {
        return needInsertCommand;
    }

    public void setNeedInsertCommand(Integer needInsertCommand) {
        this.needInsertCommand = needInsertCommand;
    }

    public Integer getHasInsertCommand() {
        return hasInsertCommand;
    }

    public void setHasInsertCommand(Integer hasInsertCommand) {
        this.hasInsertCommand = hasInsertCommand;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getAntennaName() {
        return antennaName;
    }

    public void setAntennaName(String antennaName) {
        this.antennaName = antennaName;
    }

    public boolean isRestart() {
        return isRestart;
    }

    public void setRestart(boolean restart) {
        isRestart = restart;
    }

    public Boolean getHasReceive() {
        return hasReceive;
    }

    public void setHasReceive(Boolean hasReceive) {
        this.hasReceive = hasReceive;
    }

    public Double getMaxElevation() {
        return maxElevation;
    }

    public void setMaxElevation(Double maxElevation) {
        this.maxElevation = maxElevation;
    }

    public List<ExceptionParameterInfo> getExceptionParameterInfoList() {
        return exceptionParameterInfoList;
    }

    public void setExceptionParameterInfoList(List<TelemetryExceptionPara> exceptionParaList) {
        if(exceptionParaList!=null&&exceptionParaList.size()>0){
            this.exceptionStatus="异常";
            for (TelemetryExceptionPara exceptionParam : exceptionParaList) {
                ExceptionParameterInfo exceptionParameterInfoInfo=new ExceptionParameterInfo();
                exceptionParameterInfoInfo.setSatelliteTime(exceptionParam.getSatelliteTime());
                exceptionParameterInfoInfo.setParaCode(exceptionParam.getParaCode());
                exceptionParameterInfoInfo.setParaName(exceptionParam.getParaName());
                exceptionParameterInfoInfo.setFrameName(exceptionParam.getFrameName());
                exceptionParameterInfoInfo.setThreshold(exceptionParam.getThreshold());
                exceptionParameterInfoInfo.setParaValue(exceptionParam.getParaValue());
                exceptionParameterInfoInfo.setTeleplanId(this.telemetryPlanId);
                exceptionParameterInfoInfo.setSubsystemName(exceptionParam.getSubsystemName());
                exceptionParameterInfoInfo.setSubssystemChargerContact(exceptionParam.getSubssystemChargerContact());
                if(this.exceptionParameterInfoList==null){
                    this.exceptionParameterInfoList=new ArrayList<>();
                }
                this.exceptionParameterInfoList.add(exceptionParameterInfoInfo);
            }
        }else{
            this.exceptionStatus="正常";
        }
    }
}
