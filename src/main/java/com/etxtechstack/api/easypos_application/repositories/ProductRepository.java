package com.etxtechstack.api.easypos_application.repositories;

import com.etxtechstack.api.easypos_application.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findProductByNameIgnoreCase(String name);
    Optional<Product> findProductByBarcode(String barcode);

    @Query(value = "SELECT * FROM products WHERE LOWER(name) LIKE LOWER('%?1%') OR LOWER(barcode) LIKE LOWER('%?1')", nativeQuery = true)
    List<Product> findProductByNameOrBarcode(String filter);
}
