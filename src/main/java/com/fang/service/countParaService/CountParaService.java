package com.fang.service.countParaService;

import com.fang.database.postgresql.entity.CountPara;
import com.fang.database.postgresql.repository.CountParaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountParaService {
    @Autowired
    private CountParaRepository countParaDao;
    public List<CountPara> getParaCountBySatelliteName(String satelliteName){
        return this.countParaDao.findBySatelliteName(satelliteName);
    }
    public void saveParaCount(List<CountPara> paraCountList){
        if(paraCountList!=null)
        {
            this.countParaDao.saveAll(paraCountList);
        }
    }
}
