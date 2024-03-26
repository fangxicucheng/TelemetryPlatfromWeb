package com.fang.database.postgresql.repository;

import com.fang.database.postgresql.entity.ExceptionFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExceptionFileRepository extends JpaRepository<ExceptionFile,Integer> {
}
