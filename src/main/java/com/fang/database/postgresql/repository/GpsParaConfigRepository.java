package com.fang.database.postgresql.repository;

import com.fang.database.postgresql.entity.GpsParaConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GpsParaConfigRepository extends JpaRepository<GpsParaConfig,Integer> {
}
