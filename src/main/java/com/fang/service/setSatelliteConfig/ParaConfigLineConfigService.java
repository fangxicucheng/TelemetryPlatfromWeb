package com.fang.service.setSatelliteConfig;


import com.fang.database.postgresql.entity.FrameDb;
import com.fang.database.postgresql.entity.ParaConfigLineDb;
import com.fang.database.postgresql.repository.FrameDbRepository;
import com.fang.database.postgresql.repository.ParaConfigLineDbRepository;
import com.fang.telemetry.satelliteConfigModel.TeleFrameDbModel;
import com.fang.telemetry.satelliteConfigModel.TeleParaConfigLineDbModel;
import com.fang.telemetry.satelliteConfigModel.TeleParaConfigLineDbModelInterface;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ParaConfigLineConfigService {

    @Autowired
    private ParaConfigLineDbRepository paraConfigLineDbRepository;

    @Autowired
    private FrameDbRepository frameDbRepository;


   public List<TeleParaConfigLineDbModel> getTeleParaConfigLineDbModelList(Integer frameId){
        List<TeleParaConfigLineDbModelInterface> paraConfigLineDbList = this.paraConfigLineDbRepository.getParaConfigLineDbByFrameId(frameId);
        List<TeleParaConfigLineDbModel>result=new ArrayList<>();
        if(paraConfigLineDbList!=null&&paraConfigLineDbList.size()>0){
            for (TeleParaConfigLineDbModelInterface paraConfigLineDb : paraConfigLineDbList) {
                result.add(new TeleParaConfigLineDbModel(paraConfigLineDb));
            }
        }
        return result;
    }
    public List<TeleParaConfigLineDbModel> updateTeleParaConfigLineList(Integer frameId,List<TeleParaConfigLineDbModel> paraConfigLineDbModelList){
       FrameDb frame=frameDbRepository.findById(frameId).get();
        Map<String, TeleParaConfigLineDbModel> teleParaConfigLineDbModelMap=new HashMap<>();
        for (int i = 0; i < paraConfigLineDbModelList.size(); i++) {
            TeleParaConfigLineDbModel teleParaConfigLineDbModel=  paraConfigLineDbModelList.get(i);
            teleParaConfigLineDbModel.setNum(i+1);
            if(!StringUtil.isNullOrEmpty(teleParaConfigLineDbModel.getParaCode())&&!teleParaConfigLineDbModelMap.containsKey(teleParaConfigLineDbModel.getParaCode()) ){
                teleParaConfigLineDbModelMap.put(teleParaConfigLineDbModel.getParaCode(),teleParaConfigLineDbModel);
            }
        }


        Set<String> paraCodeDbSet=new HashSet<>();

        for (int i = frame.getParaConfigLineDbList().size() - 1; i >= 0; i--) {
            ParaConfigLineDb paraConfigLineDb = frame.getParaConfigLineDbList().get(i);
            if(teleParaConfigLineDbModelMap.containsKey(paraConfigLineDb.getParaCode())){
                paraConfigLineDb.reflash(teleParaConfigLineDbModelMap.get(paraConfigLineDb.getParaCode()));
                paraCodeDbSet.add(paraConfigLineDb.getParaCode());
            }else
            {
                frame.getParaConfigLineDbList().remove(i);
            }
        }
        for (TeleParaConfigLineDbModel teleParaConfigLineDbModel : paraConfigLineDbModelList) {

            if(!StringUtil.isNullOrEmpty(teleParaConfigLineDbModel.getParaCode())&&!paraCodeDbSet.contains(teleParaConfigLineDbModel.getParaCode())){
                frame.getParaConfigLineDbList().add(new ParaConfigLineDb(teleParaConfigLineDbModel));
            }
        }

        frame.getParaConfigLineDbList().sort((o1,o2)->{
            return o1.getNum()-o2.getNum();
        });

        frameDbRepository.saveAndFlush(frame);

        return getTeleParaConfigLineDbModelList(frameId);
    }
}
