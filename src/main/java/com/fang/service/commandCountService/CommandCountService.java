package com.fang.service.commandCountService;

import com.fang.database.postgresql.entity.CommandCount;
import com.fang.database.postgresql.repository.CommandCountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        this.commandCountDao.save(commandCount);
    }
    public void saveOrUpdateCommandCountList(List<CommandCount> commandCountList){
        this.commandCountDao.saveAll(commandCountList);
    }


}
