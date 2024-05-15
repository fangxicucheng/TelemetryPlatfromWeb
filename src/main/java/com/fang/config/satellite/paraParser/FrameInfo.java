package com.fang.config.satellite.paraParser;

import com.fang.config.satellite.configStruct.FrameConfigClass;
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
    private FrameConfigClass frameConfigClass;
    private Integer frameFlag;
    private byte[] dataBytes;
    private boolean isValid;
    private Integer serialNum;//序列号
}
