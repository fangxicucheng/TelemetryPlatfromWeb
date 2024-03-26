package com.fang.database.postgresql.entity;

import com.fang.database.mysql.entity.TeleParaConfigLineMq;
import com.fang.telemetry.satelliteConfigModel.TeleFrameDbModel;
import com.fang.telemetry.satelliteConfigModel.TeleParaConfigLineDbModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="cg_telemetry_para_configline_config")
public class ParaConfigLineDb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false,unique = true)
    private int id;
    @Column(name="para_code")
    private String paraCode;
    @Column(name="para_name")
    private String paraName;
    @Column(name="bit_start")
    private Integer bitStart;
    @Column(name="bit_num")
    private Integer bitNum;
    @Column(name="dimension")
    private String dimension;
    @Column(name="parse_type")
    private Integer parseType;
    @Column(name="source_code_save_type")
    private String sourceCodeSaveType;
    @Column(name="handle_type")
    private String handleType;
    @Column(name="handle_param",length = 2048)
    private String handleParam;
    @Column(name="unit")
    private String unit;
    @Column(name="round")
    private Integer round;
    @Column(name="comment")
    private String comment;
    @Column(name="visual")
    private Integer visual;
    @Column(name="num")
    private Integer num;
    @Column(name="exp")
    private String exp;
    @Column(name="subsystem")
    private String subSystem;

    public ParaConfigLineDb(TeleParaConfigLineMq paraConfigLineMq) {
        this.paraCode=paraConfigLineMq.getParaCode();
        this.paraName=paraConfigLineMq.getParaName();
        this.bitStart=paraConfigLineMq.getBitStart();
        this.bitNum=paraConfigLineMq.getBitNum();
        this.dimension=paraConfigLineMq.getDimension();
        this.parseType=paraConfigLineMq.getParseType();
        this.sourceCodeSaveType=paraConfigLineMq.getSourceCodeSaveType();
        this.handleType=paraConfigLineMq.getHandleType();
        this.handleParam=paraConfigLineMq.getHandleParam();
        this.unit=paraConfigLineMq.getUnit();
        this.round=paraConfigLineMq.getRound();
        this.comment=paraConfigLineMq.getComment();
        this.visual=paraConfigLineMq.getVisual();
        this.num=paraConfigLineMq.getNum();
        this.exp=paraConfigLineMq.getExp();
        this.subSystem=paraConfigLineMq.getSubSystem();
    }
    public ParaConfigLineDb(TeleParaConfigLineDbModel configLineDbModel) {
        this.paraCode=configLineDbModel.getParaCode();
        this.paraName=configLineDbModel.getParaName();
        this.bitStart=configLineDbModel.getBitStart();
        this.bitNum=configLineDbModel.getBitNum();
        this.dimension=configLineDbModel.getDimension();
        this.parseType=configLineDbModel.getParseType();
        this.sourceCodeSaveType=configLineDbModel.getSourceCodeSaveType();
        this.handleType=configLineDbModel.getHandleType();
        this.handleParam=configLineDbModel.getHandleParam();
        this.unit=configLineDbModel.getUnit();
        this.round=configLineDbModel.getRound();
        this.comment=configLineDbModel.getComment();
        this.visual=configLineDbModel.getVisual();
        this.num=configLineDbModel.getNum();
        this.exp=configLineDbModel.getExp();
        this.subSystem=configLineDbModel.getSubSystem();
    }

    public void reflash(TeleParaConfigLineDbModel configLineDbModel){

        this.paraCode=configLineDbModel.getParaCode();
        this.paraName=configLineDbModel.getParaName();
        this.bitStart=configLineDbModel.getBitStart();
        this.bitNum=configLineDbModel.getBitNum();
        this.dimension=configLineDbModel.getDimension();
        this.parseType=configLineDbModel.getParseType();
        this.handleParam=configLineDbModel.getHandleParam();
        this.sourceCodeSaveType=configLineDbModel.getSourceCodeSaveType();
        this.handleType=configLineDbModel.getHandleType();
        this.num=configLineDbModel.getNum();

    }
}
