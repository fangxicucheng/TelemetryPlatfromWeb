package com.fang.database.mysql.repository;

import com.fang.database.mysql.entity.TeleFrameCatalogMq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeleFrameCatalogMqRepository extends JpaRepository<TeleFrameCatalogMq,Integer> {
}
