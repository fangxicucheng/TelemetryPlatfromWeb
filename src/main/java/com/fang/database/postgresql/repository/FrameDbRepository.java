package com.fang.database.postgresql.repository;

import com.fang.database.postgresql.entity.FrameDb;
import com.fang.telemetry.satelliteConfigModel.TeleFrameDbModelInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FrameDbRepository  extends JpaRepository<FrameDb,Integer> {
    @Query(value="select id as id,frame_code as frameCode,frame_name as frameName,num as num,reuse_channel as reuseChannel, catalog_id as catalogId from cg_telemetry_frame_config where catalog_id=?1",nativeQuery = true)
    public List<TeleFrameDbModelInterface> getFrameByCatalogId(Integer catalogId);


    @Transactional
    @Modifying
    @Query(value="update cg_telemetry_frame_config set frame_code=?1,frame_name=?2,reuse_channel=?3,num=?4 where id =?5 ",nativeQuery = true)
    public void updateFrameByFrameInfo(int frameCode ,String frameName,int reuseChannel,int num,int id);
}
