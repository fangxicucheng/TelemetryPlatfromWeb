package com.fang.database.postgresql.repository;

import com.fang.database.postgresql.entity.SatelliteNameConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SatelliteNameConfigRepository  extends JpaRepository<SatelliteNameConfig,Integer> {
}
