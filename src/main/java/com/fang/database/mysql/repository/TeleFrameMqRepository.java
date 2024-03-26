package com.fang.database.mysql.repository;

import com.fang.database.mysql.entity.TeleFrameMq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeleFrameMqRepository extends JpaRepository<TeleFrameMq,Integer> {
}
