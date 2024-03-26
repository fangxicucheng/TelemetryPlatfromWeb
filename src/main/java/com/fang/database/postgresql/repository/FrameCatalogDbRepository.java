package com.fang.database.postgresql.repository;

import com.fang.database.postgresql.entity.FrameCatalogDb;
import com.fang.telemetry.satelliteConfigModel.TeleFrameCatalogDbModel;
import com.fang.telemetry.satelliteConfigModel.TeleFrameCatalogDbModelInterface;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FrameCatalogDbRepository extends JpaRepository<FrameCatalogDb,Integer> {
    @Query(value= "select id as id, catalog_name as catalogName,catalog_code as catalogCode ,num as num,satellite_id as satelliteId from cg_telemetry_frame_catalog_config  where satellite_id = ?1 ;",nativeQuery = true)
    public List<TeleFrameCatalogDbModelInterface> getFrameCatalogDbModelListByBId( int id);
//    @Query(value= "select `id` as id, `catalog_name` as catalogName,`catalog_code` as catalogCode ,`num` as num,`satellite_id` as satelliteId from cg_telemetry_frame_catalog_config  where satellite_id=?1;",nativeQuery = true)
//    public List<Object[]> getFrameCatalogDbModelListByBId2( Integer id);
    @Query(value= "select id, catalog_name , catalog_code , num, satellite_id from cg_telemetry_frame_catalog_config  where satellite_id=?1 ;",nativeQuery = true)
    public List<Object[]> getFrameCatalogDbModelListByBId2( int id);
    @Transactional
    @Modifying
    @Query(value="update cg_telemetry_frame_catalog_config set catalog_name=?2,catalog_code=?3,num=?4 where id=?1",nativeQuery = true)
    public void updateFrameCatalogById(int id,String catalogName,int catalogCode,int num);
}
