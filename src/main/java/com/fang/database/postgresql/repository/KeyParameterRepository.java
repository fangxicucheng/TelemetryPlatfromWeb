package com.fang.database.postgresql.repository;

import com.fang.database.postgresql.entity.KeyParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyParameterRepository extends JpaRepository<KeyParameter, Integer> {
}
