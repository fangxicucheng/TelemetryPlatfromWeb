package com.fang.telemetry.satelliteConfigModel;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeleParaConfigLineDbModel {
    private int id;

    private String paraCode;

    private String paraName;

    private Integer bitStart;

    private Integer bitNum;

    private String dimension;

    private Integer parseType;

    private String sourceCodeSaveType;

    private String handleType;

    private String handleParam;

    private String unit;

    private Integer round;

    private String comment;

    private Integer visual;

    private Integer num;

    private String exp;

    private String subSystem;
    private Integer frameId;
    public TeleParaConfigLineDbModel(TeleParaConfigLineDbModelInterface paraConfigLine) {
        this.id=paraConfigLine.getId();
        this.paraCode=paraConfigLine.getParaCode();
        this.paraName=paraConfigLine.getParaName();
        this.bitStart=paraConfigLine.getBitStart();
        this.bitNum=paraConfigLine.getBitNum();
        this.dimension=paraConfigLine.getDimension();
        this.parseType=paraConfigLine.getParseType();
        this.sourceCodeSaveType=paraConfigLine.getSourceCodeSaveType();
        this.handleType=paraConfigLine.getHandleType();
        this.handleParam=paraConfigLine.getHandleParam();
        this.unit=paraConfigLine.getUnit();
        this.round=paraConfigLine.getRound();
        this.comment=paraConfigLine.getComment();
        this.visual=paraConfigLine.getVisual();
        this.num=paraConfigLine.getNum();
        this.exp=paraConfigLine.getExp();
        this.subSystem=paraConfigLine.getSubSystem();
        this.frameId=paraConfigLine.getFrameId();
    }
}
