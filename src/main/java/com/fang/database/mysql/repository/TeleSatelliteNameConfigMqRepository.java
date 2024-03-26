package com.fang.database.mysql.repository;

import com.fang.database.mysql.entity.TeleSatelliteNameConfigMq;
import com.fang.database.mysql.entity.TeleSatelliteNameMq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeleSatelliteNameConfigMqRepository extends JpaRepository<TeleSatelliteNameConfigMq,Integer> {
}
