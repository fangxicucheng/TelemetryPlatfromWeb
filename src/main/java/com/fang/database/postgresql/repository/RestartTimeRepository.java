package com.fang.database.postgresql.repository;

import com.fang.database.postgresql.entity.RestartTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface RestartTimeRepository extends JpaRepository<RestartTime,Integer> {
    public List<RestartTime> findRestartTimeBySatelliteName(String satelliteName);

}
