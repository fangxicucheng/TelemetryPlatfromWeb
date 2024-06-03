package com.fang.database.postgresql.repository;

import com.fang.database.postgresql.entity.SatelliteSubsystem;
import com.fang.database.postgresql.entity.StationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationInfoRepository extends JpaRepository<StationInfo, Integer> {

    //public void deleteStationInfoByStationName(String stationName);
    @Query(nativeQuery = false,value="select  stationName from StationInfo ")
    public List<String> getStationNameList();
}
