package com.fang.config.satellite.paraParser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FrameInfo {
    private String satelliteName;
    private Integer frameCode;
    private Integer catalogCode;
    private  Integer reuseChannel;
    private String frameName;
    private Integer frameFlag;
    private byte[] dataBytes;
    private boolean isValid;
    private Integer serialNum;//序列号
    private boolean is03BCBD;

}
