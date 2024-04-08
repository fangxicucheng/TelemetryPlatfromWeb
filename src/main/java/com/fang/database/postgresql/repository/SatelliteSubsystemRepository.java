package com.fang.database.postgresql.repository;

import com.fang.database.postgresql.entity.SatelliteSubsystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SatelliteSubsystemRepository extends JpaRepository<SatelliteSubsystem, Integer> {

    public List<SatelliteSubsystem> findBySatelliteName(String satelliteName);
}
