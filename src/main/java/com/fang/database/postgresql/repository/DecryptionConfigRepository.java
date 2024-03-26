package com.fang.database.postgresql.repository;

import com.fang.database.postgresql.entity.DecryptionCofnig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DecryptionConfigRepository  extends JpaRepository<DecryptionCofnig,Integer> {
}
