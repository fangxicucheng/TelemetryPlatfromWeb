package com.fang.database.postgresql.repository;

import com.fang.database.postgresql.entity.SatelliteDb;
import com.fang.telemetry.satelliteConfigModel.TeleSatelliteDbModel;
import jakarta.persistence.Column;
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
    @Query("select new com.fang.telemetry.satelliteConfigModel.TeleSatelliteDbModel(satellite.id,satellite.satelliteName,satellite.satelliteId,satellite.satelliteBytesStr,satellite.bdICCardsStr) from SatelliteDb  satellite order by satellite.id")
    public List<TeleSatelliteDbModel> querySatelliteName();


    @Query(value="update cg_telemetry_satellite set satellite_name=?2,satellite_id_code=?3,satellite_bytes=?4,bd_ic_cards=?5 where id=?1 ",nativeQuery = true)
    @Modifying
    @Transactional
    public void updateSatelliteDbInfo(int id,String satelliteName,String satelliteId,String satelliteBytesStr,String bdICCardsStr);
    @Query(value="update cg_telemetry_satellite set satellite_name=?2,satellite_id_code=?3 where id=?1 ",nativeQuery = true)
    @Modifying
    @Transactional
    public void updateSatelliteDbInfo(int id,String satelliteName,String satelliteId);

    @Query(value="SELECT satellite_name from cg_telemetry_satellite",nativeQuery = true)
    public List<String> getSatelliteNameList();



}
