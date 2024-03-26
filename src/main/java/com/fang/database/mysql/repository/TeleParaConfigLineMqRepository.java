package com.fang.database.mysql.repository;

import com.fang.database.mysql.entity.TeleParaConfigLineMq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeleParaConfigLineMqRepository extends JpaRepository<TeleParaConfigLineMq,Integer> {
}
