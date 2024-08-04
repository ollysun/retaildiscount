package com.access.zenchallenge.repository;

import com.access.zenchallenge.entity.BillEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<BillEntity, Long> {
}