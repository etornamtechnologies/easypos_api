package com.etxtechstack.api.easypos_application.repositories;

import com.etxtechstack.api.easypos_application.models.StockUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockUnitRepository extends JpaRepository<StockUnit, Integer> {
    Optional<StockUnit> findStockUnitByNameIgnoreCase(String name);
}
