package com.fang.config.satellite.paraParser;

import com.fang.config.exception.ExceptionManager;
import com.fang.config.satellite.configStruct.*;
import com.fang.database.postgresql.entity.CountPara;
import com.fang.database.postgresql.entity.SatelliteDb;
import com.fang.service.dataBaseManager.DataBaseManagerService;
import com.fang.service.setExcpetionJuge.ThresholdInfo;
import com.fang.service.telemetryService.SatelliteTimeManager;
import com.fang.telemetry.TelemetryFrameModel;
import com.fang.telemetry.TelemetryParameterModel;
import com.fang.utils.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor
@Data
public class BaseParaParser implements ParaParser {

    private String satelliteName;
    private ThreadLocal<Map<String, Double>> threadLocalMap;
    private SatelliteConfigClass satelliteConfigClass;
    private ExceptionManager exceptionManager;
    private boolean isBd;
    private ThreadLocal<SatelliteTimeManager> satelliteTimeManagerThreadLocal;
    @Override
    public String getDisplayValue(String paraCode, Double paraValue ,int frameFlag,SatelliteTimeManager satelliteTimeManager) {
        return paraValue.toString();
    }

    @Override
    public void init(String satelliteName, SatelliteDb satelliteDb, List<ThresholdInfo> thresholdInfoList) {
        this.satelliteName = satelliteName;
        this.isBd = false;
        if (this.satelliteName.contains("北斗")) {
            isBd = true;
        }
        this.threadLocalMap = new ThreadLocal<>();
        this.satelliteConfigClass = new SatelliteConfigClass(satelliteDb);
        this.exceptionManager = ConfigUtils.createExceptionManager(satelliteName);
        setExceptionManager(this.exceptionManager, thresholdInfoList);
        this.satelliteTimeManagerThreadLocal = new ThreadLocal<>();

    }

    @Override
    public FrameInfo parseFrameInfoFromBytes(byte[] receiveBytes) {
        FrameInfo frameInfo = new FrameInfo();
        if (this.isBd) {
            ParseBDUtils.parseBdFrameInfo(frameInfo, receiveBytes, this.satelliteConfigClass);
        } else {
            ParseMCUtils.parseMCFrameInfo(frameInfo, receiveBytes, this.satelliteConfigClass);
        }
        if(frameInfo.getFrameConfigClass()==null||frameInfo.getFrameConfigClass().getFrameName()==null){
            System.out.println("帧名称为空");
        }
        return frameInfo;
    }

    @Override
    public void parseTelemetryFrame( FrameInfo frameInfo, TelemetryFrameModel frame) {
        frame.setDelayFlag(frameInfo.getFrameFlag());
        frame.setCorrSate(frameInfo.isValid());
        frame.setFrameFlag(frameInfo.getFrameFlag());
        frame.setFrameNum(frameInfo.getFrameNum());
        if (!frame.isCorrSate()) {
            return;
        }
        FrameConfigClass config = frameInfo.getFrameConfigClass();
        frame.setFrameName(config.getFrameName());
        if (config != null) {
            boolean[] bitArray = getBitArray(frameInfo.getDataBytes());
            for (ParaConfigLineConfigClass configLine : config.getConfigLineList()) {
                if (configLine.getBitStart() + configLine.getBitNum() > bitArray.length) {
                    continue;
                }
                Map<String, Double> realMap = this.threadLocalMap.get();
                Long sourceCode = getSourceSourceCode(configLine, bitArray);
                String hexCodeStr = StringConvertUtils.getHexString(sourceCode);
                double paraValue = getParaValue(sourceCode, bitArray, configLine);
                String paraValueStr = getDisplay(configLine, frame.getDelayFlag(),hexCodeStr, paraValue);
                boolean isException = configLine.judgeException(paraValue, realMap);
                updateRealMap(configLine.getParaCode(), paraValue, realMap);
                TelemetryParameterModel parameterModel = new TelemetryParameterModel();
                parameterModel.setParaCode(configLine.getParaCode());
                parameterModel.setParaDouble(paraValue);
                parameterModel.setExpFlag(isException);
                parameterModel.setParaHex(hexCodeStr);
                parameterModel.setParaValue(paraValueStr);
                parameterModel.setReceiveCount(configLine.getParaCodeCount());
                frame.addParameter(parameterModel);
            }
            frame.setFrameFlag(frameInfo.getFrameFlag() );
            SatelliteTimeManager satelliteTimeManager = this.satelliteTimeManagerThreadLocal.get();
            if (frameInfo.getFrameFlag() == 0) {
                frame.setSatelliteTime(satelliteTimeManager.getSatelliteTimeStr());
                frame.setRestartTime(satelliteTimeManager.getRestartTimeStr());
            } else {
                frame.setSatelliteTime(satelliteTimeManager.getSatelliteTimeDelayStr());
                frame.setRestartTime(satelliteTimeManager.getRestartTimeDelayStr());
            }
        }
    }
    @Override
    public void setUnchangedParaValue() {
        Map<String, Double> paraCountMap = DataBaseManagerService.getParaCountMap(satelliteName);
        Map<String, ParaConfigLineConfigClass> unchangedJudgeParaMap = this.satelliteConfigClass.getUnchangedJudgeParaMap();
        if (unchangedJudgeParaMap == null || unchangedJudgeParaMap.size() == 0) {
            return;
        }
        for (ParaConfigLineConfigClass configLine : unchangedJudgeParaMap.values()) {
            if (paraCountMap.containsKey(configLine.getParaCode())) {
                configLine.getParaJudge().refresh(paraCountMap.get(configLine.getParaCode()));
            } else {
                configLine.getParaJudge().refresh(-10000.0);
            }
        }
    }


    @Override
    public Map<String, Double> getUnchangedParaValue() {
        Map<String, Double> unChangedParaValueMap = new HashMap<>();
        Map<String, ParaConfigLineConfigClass> unchangedJudgeParaMap = this.satelliteConfigClass.getUnchangedJudgeParaMap();
        for (String paraCode : unchangedJudgeParaMap.keySet()) {
            unChangedParaValueMap.put(paraCode, unchangedJudgeParaMap.get(paraCode).getParaJudgeBufferValue());
        }
        return unChangedParaValueMap;
    }

    @Override
    public void updateUnchangedParaValue() {
        Map<String, ParaConfigLineConfigClass> unchangedJudgeParaMap = this.satelliteConfigClass.getUnchangedJudgeParaMap();
        if (unchangedJudgeParaMap != null) {
            List<CountPara> countParaList = new ArrayList<>();

            for (ParaConfigLineConfigClass configLine : unchangedJudgeParaMap.values()) {

                countParaList.add(new CountPara(this.satelliteName,configLine));
            }
            DataBaseManagerService.updateSatelliteParaCountList(this.satelliteName,countParaList);
        }
    }

    private String getDisplay(ParaConfigLineConfigClass configLine, int frameFlag, String hexCodeStr, double paraValue) {
        String paraValueStr = switch (configLine.getHandleType()) {
            case 时间 -> UTCUtils.getUTCTimeStr(paraValue);
            case 源码 -> hexCodeStr;
            case 状态码 -> configLine.getParseState(paraValue);
            default -> getDisplayValue(configLine.getParaCode(), paraValue, frameFlag,this.satelliteTimeManagerThreadLocal.get());
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
                ParaConfigLineConfigClass configLine = configLineMap.get(thresholdInfo.getParaCode());
                configLine.initExceptionManager(thresholdInfo, exceptionManager);
                if (configLine.checkUnchanged()) {
                    this.satelliteConfigClass.setUnchangedPara(configLine);
                }
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

    private void updateRealMap(String paraCode, Double paraValue, Map<String, Double> realMap) {

        if (realMap.containsKey(paraCode)) {
            realMap.put(paraCode, paraValue);
        }
    }


    @Override
    public void initThread() {
        this.exceptionManager.initThread();
        this.threadLocalMap.set(getInitRealMap());
        this.satelliteTimeManagerThreadLocal.set(new SatelliteTimeManager(this.satelliteName));
        this.satelliteConfigClass.initThread();
        this.setUnchangedParaValue();

    }

    @Override
    public void destroyThread() {
        this.threadLocalMap.remove();
        this.exceptionManager.detroyThread();
        this.satelliteConfigClass.destroyTread();
        SatelliteTimeManager satelliteTimeManager = this.satelliteTimeManagerThreadLocal.get();
        satelliteTimeManager.saveRestartTime();
        this.satelliteTimeManagerThreadLocal.remove();
    }
}
