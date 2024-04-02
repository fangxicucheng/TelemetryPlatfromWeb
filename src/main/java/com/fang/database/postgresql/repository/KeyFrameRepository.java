package com.fang.database.postgresql.repository;

import com.fang.database.postgresql.entity.KeyFrame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeyFrameRepository extends JpaRepository<KeyFrame,Integer> {

    @Query(value="select * from cg_telemetry_key_frame where satellite_id=?1",nativeQuery = true)
    public List<KeyFrame> getKeyFramesBySatelliteId(Integer satelliteId);
}
