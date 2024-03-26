package com.fang.database.postgresql.repository;

import com.fang.database.postgresql.entity.ExceptionParameterInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExceptionParameterInfoRepository extends JpaRepository<ExceptionParameterInfo, Integer> {
}
