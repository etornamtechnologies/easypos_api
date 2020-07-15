package com.etxtechstack.api.easypos_application.repositories;

import com.etxtechstack.api.easypos_application.models.PurchaseEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseEntryRepository extends JpaRepository<PurchaseEntry, Integer> {

}
