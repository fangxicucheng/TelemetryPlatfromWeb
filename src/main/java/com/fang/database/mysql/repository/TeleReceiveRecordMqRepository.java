package com.fang.database.mysql.repository;

import com.fang.database.mysql.entity.TeleReceiveRecordMq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeleReceiveRecordMqRepository extends JpaRepository<TeleReceiveRecordMq,Integer> {
}
