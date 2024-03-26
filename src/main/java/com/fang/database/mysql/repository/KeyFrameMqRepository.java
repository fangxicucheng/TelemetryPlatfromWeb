package com.fang.database.mysql.repository;

import com.fang.database.mysql.entity.KeyFrameMq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyFrameMqRepository extends JpaRepository<KeyFrameMq, Integer> {
}
