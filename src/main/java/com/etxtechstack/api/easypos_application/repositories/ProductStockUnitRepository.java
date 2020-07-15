package com.etxtechstack.api.easypos_application.repositories;


import com.etxtechstack.api.easypos_application.models.Product;
import com.etxtechstack.api.easypos_application.models.ProductStockUnit;
import com.etxtechstack.api.easypos_application.models.ProductStockUnitId;
import com.etxtechstack.api.easypos_application.models.StockUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductStockUnitRepository extends JpaRepository<ProductStockUnit, ProductStockUnitId> {
    @Modifying
    @Query(value = "INSERT INTO product_stock_unit (product_id, stock_unit_id, factor, price, status) " +
            "VALUES(?1, ?2, ?3, ?4, ?5)", nativeQuery = true)
    void saveProductStockUnit(Integer productId, Integer stockUnitId, Integer factor, Integer price, String status);

    @Modifying
    @Query(value = "DELETE FROM product_stock_unit WHERE product_id=?1 AND stock_unit_id=?2", nativeQuery = true)
    void deleteProductStockUnit(Integer productId, Integer stockUnitId);

    @Modifying
    @Query(value = "UPDATE product_stock_unit SET factor=?3, price=?4, status=?5 " +
            "WHERE product_id = ?1 AND stock_unit_id=?2", nativeQuery = true)
    void updateProductStockUnit(Integer productId, Integer stockUnitId, Integer factor, Integer price, String status);

    @Query(value = "SELECT * FROM product_stock_unit WHERE product_id=?1 AND factor=1.0", nativeQuery = true)
    List<ProductStockUnit> getProductAllUnitStockUnit(Integer productId);

    public List<ProductStockUnit> findProductStockUnitsByProductAndStockUnitAndFactor(Product product, StockUnit stockUnit, Integer factor);
    public Optional<ProductStockUnit> findProductStockUnitByProductAndStockUnit(Product product, StockUnit stockUnit);
}
