package com.fang.database.postgresql.repository;


import com.fang.database.postgresql.entity.TelemetryReplay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelemetryReplayRepository extends JpaRepository<TelemetryReplay, Integer> {
}
