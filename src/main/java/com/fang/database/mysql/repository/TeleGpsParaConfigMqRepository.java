package com.fang.database.mysql.repository;

import com.fang.database.mysql.entity.TeleGpsParaConfigMq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TeleGpsParaConfigMqRepository extends JpaRepository<TeleGpsParaConfigMq,Integer> {
}
