package com.fang.database.postgresql.repository;

import com.fang.database.postgresql.entity.CountPara;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountParaRepository extends JpaRepository<CountPara,Integer> {
}
