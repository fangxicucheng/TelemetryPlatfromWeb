package com.fang.service.setExcpetionJuge;

import com.fang.database.postgresql.entity.*;
import com.fang.database.postgresql.repository.KeyFrameRepository;
import com.fang.database.postgresql.repository.SatelliteDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class KeyFrameService {

    @Autowired
    private KeyFrameRepository keyFrameRepository;
    @Autowired
    private SatelliteDbRepository satelliteDbRepository;
    public List<KeyFrame> getKeyFrameListBySatelliteId(Integer satelliteId) {
        return this.keyFrameRepository.getKeyFramesBySatelliteId(satelliteId);
    }
    public List<ParaConfigLineExceptionInfo> getKeyFrameParaConfigleLine(Integer satelliteId, Integer keyFrameId) {
        KeyFrame keyFrame = this.keyFrameRepository.findById(keyFrameId).get();
        String[] paraCodeList = keyFrame.getParaCodes().split(",");
        Map<String, ParaConfigLineExceptionInfo> paraConfigLineExceptionInfoMap = new HashMap<>();
        if (paraCodeList.length >= 1) {
            for (String paraCode : paraCodeList) {
                paraConfigLineExceptionInfoMap.put(paraCode, new ParaConfigLineExceptionInfo());
            }
        }
        List<ParaConfigLineExceptionInfo> paraConfigLineExceptionInfoList = new ArrayList<>();

        SatelliteDb satelliteDb = satelliteDbRepository.findById(satelliteId).get();
        if (satelliteDb.getFrameCatalogDbList() != null && satelliteDb.getFrameCatalogDbList().size() > 0) {
            for (FrameCatalogDb frameCatalogDb : satelliteDb.getFrameCatalogDbList()) {
                if (frameCatalogDb.getFrameDbList() != null && frameCatalogDb.getFrameDbList().size() > 0) {
                    for (FrameDb frameDb : frameCatalogDb.getFrameDbList()) {
                        if (frameDb.getParaConfigLineDbList() != null && frameDb.getParaConfigLineDbList().size() > 0) {
                            for (ParaConfigLineDb paraConfigLineDb : frameDb.getParaConfigLineDbList()) {
                                if (paraConfigLineExceptionInfoMap.containsKey(paraConfigLineDb.getParaCode())) {
                                    ParaConfigLineExceptionInfo paraConfigLineExceptionInfo = paraConfigLineExceptionInfoMap.get(paraConfigLineDb.getParaCode());
                                    paraConfigLineExceptionInfo.setParaName(paraConfigLineDb.getParaName());
                                    paraConfigLineExceptionInfo.setParaCode(paraConfigLineDb.getParaCode());
                                    paraConfigLineExceptionInfo.setId(paraConfigLineDb.getId());
                                    paraConfigLineExceptionInfo.setExp(paraConfigLineDb.getExp());
                                    paraConfigLineExceptionInfo.setSubSystem(paraConfigLineDb.getSubSystem());
                                }

                            }
                        }

                    }
                }

            }
        }
        for (String paraCode : paraCodeList) {

            paraConfigLineExceptionInfoList.add(paraConfigLineExceptionInfoMap.get(paraCode));
        }
        return paraConfigLineExceptionInfoList;


    }
}
