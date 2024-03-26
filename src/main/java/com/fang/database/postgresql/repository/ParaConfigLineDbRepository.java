package com.fang.database.postgresql.repository;

import com.fang.database.postgresql.entity.ParaConfigLineDb;
import com.fang.telemetry.satelliteConfigModel.TeleParaConfigLineDbModelInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParaConfigLineDbRepository extends JpaRepository<ParaConfigLineDb,Integer> {


    @Query(value = " select " +
            "id," +
            "para_code as paraCode," +
            "para_name as paraName," +
            "bit_start as bitStart," +
            "bit_num as bitNum," +
            "dimension as dimension," +
            "parse_type as parseType," +
            "source_code_save_type as sourceCodeSaveType," +
            "handle_type as handleType," +
            "handle_param as handleParam," +
            "unit as uint," +
            "round as round," +
            "comment as comment," +
            "visual as visual," +
            "num as num," +
            "exp as exp," +
            "subsystem as subSystem," +
            "frame_id as frameId" +
            " from cg_telemetry_para_configline_config where frame_id=?1 order by num",nativeQuery = true)
    public List<TeleParaConfigLineDbModelInterface> getParaConfigLineDbByFrameId(Integer frameId);
}
