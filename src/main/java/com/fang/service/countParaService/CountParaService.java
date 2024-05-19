package com.fang.service.countParaService;

import com.fang.database.postgresql.entity.CountPara;
import com.fang.database.postgresql.repository.CountParaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CountParaService {
    @Autowired
    private CountParaRepository countParaDao;
    public List<CountPara> getParaCountBySatelliteName(String satelliteName){
        return this.countParaDao.findBySatelliteName(satelliteName);
    }
    @Transactional
    public void saveParaCount(String satelliteName,List<CountPara>paraCountList){
        if(paraCountList==null||paraCountList.size()==0){
         return;
        }
        List<CountPara> countParaDbList = countParaDao.findBySatelliteName(satelliteName);
        Map<String,CountPara> countParaDbMap=new HashMap<>();
        for (CountPara countPara : countParaDbList) {
            if(!countParaDbMap.containsKey(countPara.getParaCode())){
                countParaDbMap.put(countPara.getParaCode(),countPara);
            }
            else
            {
                this.countParaDao.deleteById(countPara.getId());
            }
        }

        for (CountPara countPara : paraCountList) {

            if(!countParaDbMap.containsKey(countPara.getParaCode())){
                countParaDbMap.put(countPara.getParaCode(),countPara);
            }
            else
            {
                CountPara countParaDb = countParaDbMap.get(countPara.getParaCode());
                countParaDb.setParaValue(countPara.getParaValue());
                countParaDb.setDateTime(new Date());
            }

        }
        this.countParaDao.saveAll(countParaDbMap.values());
    }
}
