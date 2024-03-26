package com.fang.database.postgresql.repository;

import com.fang.database.postgresql.entity.KeyFrame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyFrameRepository extends JpaRepository<KeyFrame,Integer> {
}
