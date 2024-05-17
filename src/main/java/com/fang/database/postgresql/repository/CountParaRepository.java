package com.fang.database.postgresql.repository;

import com.fang.database.postgresql.entity.CountPara;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountParaRepository extends JpaRepository<CountPara,Integer> {

    List<CountPara> findBySatelliteName(String satelliteName);
    @Query(nativeQuery = false,value="from CountPara  where satelliteName=:satelliteName and paraCode=:paraCode")
    List<CountPara> findCountParaBySatelliteNameAndParaCode(String satelliteName,String paraCode);
}
