package com.fang.database.postgresql.repository;

import com.fang.database.postgresql.entity.CommandCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandCountRepository extends JpaRepository<CommandCount,Integer> {
}
