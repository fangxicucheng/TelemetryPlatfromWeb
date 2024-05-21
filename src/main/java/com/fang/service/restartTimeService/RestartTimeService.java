package com.fang.service.restartTimeService;

import com.fang.database.postgresql.entity.RestartTime;
import com.fang.database.postgresql.repository.RestartTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RestartTimeService {
    @Autowired
    private RestartTimeRepository restartTimeDao;
    public Date getRestartTimeBySatelliteName(String satelliteName) {
        List<RestartTime> restartTimeList = this.restartTimeDao.findRestartTimeBySatelliteName(satelliteName);
        if (restartTimeList == null || restartTimeList.size() == 0) {
            return null;
        }
        return restartTimeList.get(0).getRestartTime();

    }


    public void saveOrUpdateRestartTime(String satelliteName, Date restartDateTime) {
        List<RestartTime> restartTimeList = this.restartTimeDao.findRestartTimeBySatelliteName(satelliteName);
        if (restartTimeList != null && restartTimeList.size() == 0) {
            RestartTime restartTime = new RestartTime();
            restartTime.setRestartTime(restartDateTime);
            restartTime.setSatelliteName(satelliteName);
            this.restartTimeDao.save(restartTime);
        }

        for (int i = 0; i < restartTimeList.size(); i++) {
            RestartTime restartTime = restartTimeList.get(i);
            restartTime.setRestartTime(restartDateTime);
            this.restartTimeDao.save(restartTime);
            if (i > 0) {
                this.restartTimeDao.deleteById(restartTime.getId());
            }
        }


    }


}
