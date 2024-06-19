package com.fang.telemetry.satelliteConfigModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeleFrameCatalogDbModel {
    public TeleFrameCatalogDbModel(TeleFrameCatalogDbModelInterface frameCatalog) {
        this.id=frameCatalog.getId();
        this.catalogCode=frameCatalog.getCatalogCode();
        this.num=frameCatalog.getNum();
        this.catalogName=frameCatalog.getCatalogName();
        this.satelliteId=frameCatalog.getSatelliteId();
    }

    private Integer id;

    private String catalogName;

    private Integer num;

    private Integer catalogCode;
    private Integer satelliteId;

}
