package com.fang.database.postgresql.repository;
import com.fang.database.postgresql.entity.TelemetryPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelemetryPlanRepository extends JpaRepository<TelemetryPlan, Integer> {
}
