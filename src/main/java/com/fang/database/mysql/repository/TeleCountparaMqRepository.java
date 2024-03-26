package com.fang.database.mysql.repository;


import com.fang.database.mysql.entity.TeleCountparaMq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeleCountparaMqRepository extends JpaRepository<TeleCountparaMq, Integer> {
}
