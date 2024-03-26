package com.fang.database.postgresql.repository;

import com.fang.database.postgresql.entity.SatelliteDb;
import com.fang.telemetry.satelliteConfigModel.TeleSatelliteDbModel;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SatelliteDbRepository extends JpaRepository<SatelliteDb,Integer> {

    @Query("select satellite from SatelliteDb satellite")
    public List<SatelliteDb> queryListAll();
    @Query("select new com.fang.telemetry.satelliteConfigModel.TeleSatelliteDbModel(satellite.id,satellite.satelliteName) from SatelliteDb  satellite order by satellite.id")
    public List<TeleSatelliteDbModel> querySatelliteName();
    @Query(value="update cg_telemetry_satellite set satellite_name=?2 where id=?1 ",nativeQuery = true)
    @Modifying
    @Transactional
    public void updateSatelliteDbInfo(int id,String satelliteName);
}
