package com.fang.database.postgresql.repository;

import com.fang.database.postgresql.entity.CommandCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommandCountRepository extends JpaRepository<CommandCount,Integer> {

    List<CommandCount> findCommandCountBySatelliteName(String satelliteName);

}
