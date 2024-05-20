package com.fang.service.commandCountService;

import com.fang.config.satellite.configStruct.FrameCatalogConfigClass;
import com.fang.config.satellite.configStruct.FrameConfigClass;
import com.fang.config.satellite.configStruct.ParaConfigLineConfigClass;
import com.fang.config.satellite.configStruct.SatelliteConfigClass;
import com.fang.database.postgresql.entity.CommandCount;
import com.fang.database.postgresql.repository.CommandCountRepository;
import com.fang.utils.ConfigUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CommandCountService {
    @Autowired
    private CommandCountRepository commandCountDao;


    //获取列表
    public List<CommandCount> getCommandCountList(){

        return this.commandCountDao.findAll();
    }

    public List<CommandCount> getCommandBySatelliteName(String satelliteName){
        return this.commandCountDao.findCommandCountBySatelliteName(satelliteName);
    }

    public void saveOrUpdateCommandCount(CommandCount commandCount)
    {
        if(commandCount==null){
            return;
        }
        refresh(commandCount);
        this.commandCountDao.save(commandCount);
    }

    private void refresh(CommandCount commandCount){

        SatelliteConfigClass satelliteConfigClass = ConfigUtils.getParaParser(commandCount.getSatelliteName()).getSatelliteConfigClass();

        Map<String, FrameCatalogConfigClass> catalogNameConfigClassMap = satelliteConfigClass.getCatalogNameConfigClassMap();
        if (catalogNameConfigClassMap!=null) {

            for (FrameCatalogConfigClass catalogConfigClass : catalogNameConfigClassMap.values()) {

                Map<String, FrameConfigClass> frameNameMap = catalogConfigClass.getFrameNameMap();
                if(frameNameMap!=null){
                    for (FrameConfigClass frame : frameNameMap.values()) {

                        List<ParaConfigLineConfigClass> configLineList = frame.getConfigLineList();
                        if(configLineList!=null){
                            for (ParaConfigLineConfigClass configLine : configLineList) {

                                if (configLine.getParaCode().equals(commandCount.getTotalCommandParaCode())) {
                                    commandCount.setTotalCommandCountMax((int)Math.pow(2,configLine.getBitNum()));
                                    commandCount.setTotalCommandParaName(configLine.getParaName());
                                }
                                if(configLine.getParaCode().equals(commandCount.getErrorCommandParaCode())){
                                    commandCount.setTotalCommandParaName(configLine.getParaName());
                                    commandCount.setErrorCommandCountMax((int)Math.pow(2,configLine.getBitNum()));
                                }

                            }
                        }
                    }
                }

            }
        }
    }
    public void saveOrUpdateCommandCountList(List<CommandCount> commandCountList){
        if(commandCountList==null||commandCountList.size()==0){
            return;
        }
        for (CommandCount commandCount : commandCountList) {
            refresh(commandCount);
        }

        this.commandCountDao.saveAll(commandCountList);
    }
    public void deleteCommandCountById(Integer id){
        this.commandCountDao.deleteById(id);
    }


}
