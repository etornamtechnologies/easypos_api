package com.etxtechstack.api.easypos_application.repositories;

import com.etxtechstack.api.easypos_application.models.SaleEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleEntryRepository extends JpaRepository<SaleEntry, Integer> {

}
