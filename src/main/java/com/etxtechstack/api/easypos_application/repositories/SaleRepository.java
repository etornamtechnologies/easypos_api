package com.etxtechstack.api.easypos_application.repositories;

import com.etxtechstack.api.easypos_application.models.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Integer> {
    @Query(value = "SELECT COUNT(id) FROM sales WHERE to_date(CAST(created_at AS TEXT), 'yyyy/mm/dd') = to_date(CAST(?1 AS TEXT), 'yyyy/mm/dd')",
            nativeQuery = true)
    Integer getSaleCountByDate(Date date);
}
