package com.fang.database.mysql.repository;

import com.fang.database.mysql.entity.TelePlanMq;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelePlanMqRepository extends JpaRepository<TelePlanMq, Integer> {
}
