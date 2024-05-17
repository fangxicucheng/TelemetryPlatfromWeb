package com.fang.config.satellite.configStruct;

import com.fang.database.postgresql.entity.FrameCatalogDb;
import com.fang.database.postgresql.entity.SatelliteDb;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SatelliteConfigClass {
    private String satelliteName;
    private String satelliteId;
    private boolean isBCBDSatellite;
    private boolean hasSixBitWidthFrameCode;
    private boolean isGPSatellite;
    private boolean isKDJD;
    private boolean isBD;
    private Map<Integer, FrameCatalogConfigClass> catalogCodeConfigClassMap;
    private Map<String, FrameCatalogConfigClass> catalogNameConfigClassMap;
    //恒不变参数
    private Map<String,ParaConfigLineConfigClass>unchangedJudgeParaMap;
    public SatelliteConfigClass(SatelliteDb satelliteDb) {
        this.satelliteName = satelliteDb.getSatelliteName();
        this.unchangedJudgeParaMap=new HashMap<>();
        this.satelliteId = satelliteDb.getSatelliteId();
        checkSixBitWidthFrameCode();
        checkKDJD();
        checkBDSatellite();
        checkGF03BCBDSatellite();
        checkGPSatellite();
        for (FrameCatalogDb frameCatalogDb : satelliteDb.getFrameCatalogDbList()) {
            FrameCatalogConfigClass frameCatalogConfigClass = new FrameCatalogConfigClass(frameCatalogDb);
            if (this.catalogCodeConfigClassMap == null) {
                this.catalogCodeConfigClassMap = new HashMap<>();
            }
            if (this.catalogNameConfigClassMap == null) {
                this.catalogNameConfigClassMap = new HashMap<>();
            }
            this.catalogNameConfigClassMap.put(frameCatalogDb.getCatalogName(), frameCatalogConfigClass);
            this.catalogCodeConfigClassMap.put(frameCatalogDb.getCatalogCode(), frameCatalogConfigClass);
        }
    }
    public void initThread() {
        Collection<FrameCatalogConfigClass> catalogList = this.getCatalogNameConfigClassMap().values();
        if (catalogList != null) {
            for (FrameCatalogConfigClass catalog : catalogList) {
                catalog.initTread();
            }
        }
    }

    public void destroyTread() {
        Collection<FrameCatalogConfigClass> catalogList = this.getCatalogNameConfigClassMap().values();
        if (catalogList != null) {
            for (FrameCatalogConfigClass catalog : catalogList) {
                catalog.destroyThread();
            }
        }
    }

    private void checkBDSatellite() {
        if (this.satelliteName.contains("北斗")) {
            this.isBD = true;
        } else {
            this.isBD = false;
        }
    }

    private void checkGF03BCBDSatellite() {
        if (this.isBD && this.satelliteName.contains("高分03B") && this.satelliteName.contains("高分03C")) {
            this.isBCBDSatellite = true;
        } else {
            this.isBCBDSatellite = false;
        }
    }

    private void checkGPSatellite() {
        if (this.satelliteName.contains("光谱")) {
            this.isGPSatellite = true;
        } else {
            this.isGPSatellite = false;
        }
    }

    private void checkKDJD() {
        if (this.satelliteName.contains("宽带节点")) {
            this.isKDJD = true;
        } else {
            this.isKDJD = false;
        }
    }

    private void checkSixBitWidthFrameCode() {
        if (satelliteName.contains("宽幅") ||
                satelliteName.contains("吉星") ||
                satelliteName.contains("平台") ||
                satelliteName.contains("魔方") ||
                satelliteName.contains("高分04B") ||
                satelliteName.contains("高分05") ||
                satelliteName.contains("高分06") ||
                satelliteName.contains("TEE")
        ) {
            hasSixBitWidthFrameCode = true;
        } else {
            hasSixBitWidthFrameCode = true;
        }

    }

    public void setUnchangedPara(ParaConfigLineConfigClass configLine){
        this.unchangedJudgeParaMap.put(configLine.getParaCode(),configLine);

    }



    public FrameConfigClass getFrameConfigClassByFrameCode(Integer catalogCode, Integer frameCode, Integer refuseChannel) {
        FrameConfigClass frameConfigClass = null;
        if (this.catalogCodeConfigClassMap.containsKey(catalogCode)) {
            frameConfigClass = this.catalogCodeConfigClassMap.get(catalogCode).getFrameConfigClassByFrameCode(frameCode, refuseChannel);
        }
        return frameConfigClass;

    }

    public FrameConfigClass getFrameConfigClassByFrameName(String frameName) {
        FrameConfigClass frameConfigClass = null;
        for (FrameCatalogConfigClass catalog : this.catalogNameConfigClassMap.values()) {

            FrameConfigClass frameConfigByFrameName = catalog.getFrameConfigByFrameName(frameName);
            if (frameConfigByFrameName != null) {
                frameConfigClass = frameConfigByFrameName;
                break;
            }
        }
        return frameConfigClass;
    }
}
