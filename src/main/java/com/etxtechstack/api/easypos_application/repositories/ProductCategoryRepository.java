package com.etxtechstack.api.easypos_application.repositories;

import com.etxtechstack.api.easypos_application.models.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
    Optional<ProductCategory> findProductCategoryByNameIgnoreCase(String name);
}
