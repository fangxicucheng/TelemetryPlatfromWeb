package com.fang.telemetry.satelliteConfigModel;


import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConfigFileInfoClass {
    private Integer frameCode;
    private String frameName;
    private Integer catalogCode;
    private boolean hasInit;
    private Integer refuseChannel;

    public ConfigFileInfoClass(String fileName) {
        String[] fileNameInfo = fileName.split(" ")[1].split("\\.")[0].split("_");

        if (fileNameInfo.length == 4) {
            this.frameName = fileNameInfo[0].substring(0, fileNameInfo[0].length() - 1);
            this.refuseChannel = Integer.parseInt(fileNameInfo[0].substring(fileNameInfo[0].length() - 1, fileNameInfo[0].length()));
            this.frameCode = Integer.parseInt(fileNameInfo[1]);
            this.catalogCode = Integer.parseInt(fileNameInfo[2]);
        }
        hasInit = true;
    }


}
