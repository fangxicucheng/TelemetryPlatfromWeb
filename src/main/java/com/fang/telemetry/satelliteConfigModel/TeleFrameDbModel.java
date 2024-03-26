package com.fang.telemetry.satelliteConfigModel;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeleFrameDbModel {
    private int id;

    private String frameName;

    private Integer num;

    private Integer frameCode;
    private Integer reuseChannel;
    private Integer catalogId;

    public TeleFrameDbModel(TeleFrameDbModelInterface frame) {
        this.id=frame.getId();
        this.frameName=frame.getFrameName();
        this.frameCode=frame.getFrameCode();
        this.num=frame.getNum();
        this.reuseChannel=frame.getReuseChannel();
        this.catalogId=frame.getCatalogId();
    }
}
