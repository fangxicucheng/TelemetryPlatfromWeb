package com.fang.database.postgresql.repository;

import com.fang.database.postgresql.entity.RequestTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestTimeRepository extends JpaRepository<RequestTime, Integer> {
}
