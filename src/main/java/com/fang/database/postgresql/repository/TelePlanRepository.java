package com.fang.database.postgresql.repository;

import com.fang.database.postgresql.entity.TelePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelePlanRepository extends JpaRepository<TelePlan,Integer> {
}
