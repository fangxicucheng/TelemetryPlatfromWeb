package com.fang.database.postgresql.repository;

import com.fang.database.postgresql.entity.SatelliteSubsystem;
import com.fang.database.postgresql.entity.StationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationInfoRepository extends JpaRepository<StationInfo, Integer> {
}
