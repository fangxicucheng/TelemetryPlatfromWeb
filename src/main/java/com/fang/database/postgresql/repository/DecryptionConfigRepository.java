package com.fang.database.postgresql.repository;

import com.fang.database.postgresql.entity.DecryptionConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DecryptionConfigRepository  extends JpaRepository<DecryptionConfig,Integer> {
}
