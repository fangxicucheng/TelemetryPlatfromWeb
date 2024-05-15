package com.fang.config.satellite.paraParser;

import com.fang.config.exception.ExceptionManager;
import com.fang.config.satellite.configStruct.FrameCatalogConfigClass;
import com.fang.config.satellite.configStruct.FrameConfigClass;
import com.fang.config.satellite.configStruct.ParaConfigLineConfigClass;
import com.fang.config.satellite.configStruct.SatelliteConfigClass;
import com.fang.database.postgresql.entity.SatelliteDb;
import com.fang.service.setExcpetionJuge.ThresholdInfo;
import com.fang.telemetry.TelemetryFrame;
import com.fang.telemetry.TelemetryParameterModel;
import com.fang.utils.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor
@Data
public class BaseParaParser implements ParaParser {
    private ThreadLocal<String> satelliteTime;
    private ThreadLocal<String> satelliteTime_延时;
    private ThreadLocal<String> restartTime;
    private ThreadLocal<String> restartTime_延时;
    private String satelliteName;
    private SatelliteConfigClass satelliteConfigClass;
    private ExceptionManager exceptionManager;
    private boolean isBd;

    @Override
    public String getDisplayValue(String paraCode, Double paraValue) {
        return paraValue.toString();
    }

    @Override
    public void init(String satelliteName, SatelliteDb satelliteDb, List<ThresholdInfo> thresholdInfoList) {
        this.satelliteName = satelliteName;
        this.isBd = false;

        if (this.satelliteName.contains("北斗")) {
            isBd = true;
        }
        this.satelliteConfigClass = new SatelliteConfigClass(satelliteDb);
        this.exceptionManager = ConfigUtils.createExceptionManager(satelliteName);
        setExceptionManager(this.exceptionManager, thresholdInfoList);
        if(this.restartTime_延时==null){
            this.restartTime_延时=new ThreadLocal<>();
        }
        if(this.satelliteTime_延时==null){
            this.satelliteTime_延时=new ThreadLocal<>();
        }
        if(this.restartTime==null){
            this.restartTime=new ThreadLocal<>();
        }
        if(this.satelliteTime==null){
            this.satelliteTime=new ThreadLocal<>();
        }
    }

    @Override
    public FrameInfo parseFrameInfoFromBytes(byte[] receiveBytes) {
        FrameInfo frameInfo = new FrameInfo();
        if (this.isBd) {
            ParseBDUtils.parseBdFrameInfo(frameInfo, receiveBytes, this.satelliteConfigClass);
        } else {
            ParseMCUtils.parseMCFrameInfo(frameInfo, receiveBytes, this.satelliteConfigClass);
        }
        return frameInfo;
    }

    @Override
    public void parseTelemetryFrame(byte[] dataBytes, FrameInfo frameInfo,TelemetryFrame frame) {

        frame.setDelayFlag(frameInfo.getFrameFlag());
        frame.setCorrSate(frameInfo.isValid());
        FrameConfigClass config = frameInfo.getFrameConfigClass();
        if (config != null) {
            boolean[] bitArray = getBitArray(dataBytes);
            for (ParaConfigLineConfigClass configLine : config.getConfigLineList()) {
                if (configLine.getBitStart() + configLine.getBitNum() > bitArray.length) {
                    continue;
                }
                Long sourceCode = getSourceSourceCode(configLine, bitArray);
                String hexCodeStr = StringConvertUtils.getHexString(sourceCode);
                double paraValue = getParaValue(sourceCode, bitArray, configLine);
                String paraValueStr=getDisplay(configLine,hexCodeStr,paraValue);
                TelemetryParameterModel parameterModel=new TelemetryParameterModel();
                parameterModel.setParaCode(configLine.getParaCode());
                parameterModel.setParaDouble(paraValue);
                parameterModel.setParaHex(hexCodeStr);
                parameterModel.setParaValue(paraValueStr);
                parameterModel.setReceiveCount(configLine.getParaCodeCount());
                frame.addParameter(parameterModel);
            }
        }
    }

    private String getDisplay(ParaConfigLineConfigClass configLine, String hexCodeStr, double paraValue) {
        String paraValueStr = switch (configLine.getHandleType()) {
            case 时间 -> UTCUtils.getUTC8PLUSTimeStr(paraValue);
            case 源码 -> hexCodeStr;
            default -> getDisplayValue(configLine.getParaCode(), paraValue);
        };
        return paraValueStr;
    }


    private boolean[] getBitArray(byte[] dataBytes) {
        return ParseUtils.getBitArray(dataBytes);
    }

    private Long getSourceSourceCode(ParaConfigLineConfigClass configLine, boolean[] bitArray) {
        return ParseUtils.getSourceCode(configLine, bitArray);
    }


    private double getParaValue(Long sourceCode, boolean[] bitArray, ParaConfigLineConfigClass configLine) {
        double paraValue = sourceCode;
        paraValue = switch (configLine.getSourceCodeSaveType()) {
            case 有符号位 -> ParseUtils.parseTypeOne(bitArray, configLine);
            case 无符号 -> switch (configLine.getHandleType()) {
                case 单精度浮点数 -> ParseUtils.parseIEEE754(sourceCode);
                default -> paraValue;
            };
            case 补码 -> ParseUtils.parseTowsComplement(bitArray, configLine);
        };
        paraValue = paraValue * configLine.getDimension();
        paraValue = getSpecialFormulaValue(configLine.getParaCode(), paraValue);

        return paraValue;
    }

    private String getHexValue(Long sourceCode) {
        return StringConvertUtils.getHexString(sourceCode);
    }

    private void setExceptionManager(ExceptionManager exceptionManager, List<ThresholdInfo> thresholdInfoList) {
        Map<String, ParaConfigLineConfigClass> configLineMap = new HashMap<>();
        Map<String, FrameCatalogConfigClass> catalogMap = this.satelliteConfigClass.getCatalogNameConfigClassMap();
        if (catalogMap == null || catalogMap.size() == 0) {
            return;
        }

        for (FrameCatalogConfigClass catalog : catalogMap.values()) {
            Map<String, FrameConfigClass> frameMap = catalog.getFrameNameMap();
            if (frameMap == null || frameMap.size() == 0) {
                continue;
            }
            for (FrameConfigClass frame : frameMap.values()) {
                List<ParaConfigLineConfigClass> configLineList = frame.getConfigLineList();
                if (configLineList == null || configLineList.size() == 0) {
                    continue;
                }
                for (ParaConfigLineConfigClass configLine : configLineList) {
                    configLineMap.put(configLine.getParaCode(), configLine);
                }
            }
        }

        for (ThresholdInfo thresholdInfo : thresholdInfoList) {

            if (configLineMap.containsKey(thresholdInfo.getParaCode())) {
                configLineMap.get(thresholdInfo.getParaCode()).initExceptionManager(thresholdInfo, exceptionManager);
            }

        }
    }


    @Override
    public void setSatelliteConfigClass(SatelliteConfigClass satelliteConfigClass) {

        this.satelliteConfigClass = satelliteConfigClass;
    }

    @Override
    public SatelliteConfigClass getSatelliteConfigClass() {
        return this.satelliteConfigClass;
    }

    @Override
    public FrameConfigClass getFrameConfigClass(Integer catalogCode, Integer frameCode, Integer reuseChannel) {
        return this.satelliteConfigClass.getFrameConfigClassByFrameCode(catalogCode, frameCode, reuseChannel);
    }


    @Override
    public void threadInit() {


    }

    @Override
    public void threadFinsh() {

    }
}
