package com.fang.database.mysql.repository;

import com.fang.database.mysql.entity.TeleRestartTimeMq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeleRestartTimeRecordMqRepository extends JpaRepository<TeleRestartTimeMq,Integer> {
}
