package com.fang.database.mysql.repository;

import com.fang.database.mysql.entity.TeleSubsystemMq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeleSubsystemMqRepository extends JpaRepository<TeleSubsystemMq,Integer> {
}
