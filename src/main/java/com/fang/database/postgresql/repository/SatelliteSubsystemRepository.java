package com.fang.database.postgresql.repository;

import com.fang.database.postgresql.entity.SatelliteSubsystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SatelliteSubsystemRepository extends JpaRepository<SatelliteSubsystem, Integer> {

    public List<SatelliteSubsystem> findBySatelliteName(String satelliteName);


    @Query("DELETE FROM SatelliteSubsystem subSystem where subSystem.satelliteName=:satelliteName")
    @Modifying
    @Transactional
    public void deleteBySatelliteName(String satelliteName);



}
