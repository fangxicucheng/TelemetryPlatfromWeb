package com.fang.database.postgresql.repository;

import com.fang.database.postgresql.entity.RestartTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestartTimeRepository extends JpaRepository<RestartTime,Integer> {
}
