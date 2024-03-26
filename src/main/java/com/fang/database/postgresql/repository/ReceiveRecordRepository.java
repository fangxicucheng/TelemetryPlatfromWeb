package com.fang.database.postgresql.repository;

import com.fang.database.postgresql.entity.ReceiveRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiveRecordRepository extends JpaRepository<ReceiveRecord,Integer> {
}
