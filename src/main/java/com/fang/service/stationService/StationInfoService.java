package com.fang.service.stationService;

import com.fang.database.postgresql.entity.StationInfo;
import com.fang.database.postgresql.repository.StationInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationInfoService {
    @Autowired
    private StationInfoRepository stationInfoRepository;

    public void addOrUpdateStationIfo(StationInfo stationInfo) {

        this.stationInfoRepository.save(stationInfo);
    }

    public void addOrUpdateStationInfoList(List<StationInfo> stationInfoList) {
        this.stationInfoRepository.saveAll(stationInfoList);
    }

    public List<StationInfo> getStationInfoList() {
        return this.stationInfoRepository.findAll();
    }

    public void deleteStationInfo(Integer id) {
        this.stationInfoRepository.deleteById(id);
    }


}
