package com.etxtechstack.api.easypos_application.utils;

import com.etxtechstack.api.easypos_application.dos.ProductCategorySimple;
import com.etxtechstack.api.easypos_application.dos.ProductSimple;
import com.etxtechstack.api.easypos_application.dos.ProductStockUnitSimple;
import com.etxtechstack.api.easypos_application.dos.StockUnitSimple;
import com.etxtechstack.api.easypos_application.models.Product;
import com.etxtechstack.api.easypos_application.models.ProductStockUnit;

import java.util.List;
import java.util.stream.Collectors;

public class MapperUtil {
    public static ProductSimple MapProductToProductSimple(Product product) {
        ProductCategorySimple categorySimple = new ProductCategorySimple(product.getProductCategory().getId(),
                product.getProductCategory().getName(), product.getProductCategory().getDescription());
        StockUnitSimple stockUnitSimple = new StockUnitSimple(product.getDefaultStockUnit().getId(), product.getDefaultStockUnit().getName());
        List<ProductStockUnitSimple> productStockUnits = product.getProductStockUnits().stream().map(productStockUnit -> {
            return MapProductStockUnitToProductStockUnitSimple(productStockUnit);
        }).collect(Collectors.toList());
        ProductSimple productSimple = new ProductSimple(product.getId(), product.getName(), product.getBarcode(), product.getSize(),
                product.getColor(), product.getDescription(), product.getCreatedAt(), categorySimple, stockUnitSimple, product.getStockQuantity(),
                productStockUnits);
        return productSimple;
    }

    public static ProductStockUnitSimple MapProductStockUnitToProductStockUnitSimple(ProductStockUnit productStockUnit) {
        ProductSimple product = new ProductSimple();
        product.setId(productStockUnit.getProduct().getId());
        product.setName(productStockUnit.getProduct().getName());
        StockUnitSimple stockUnit = new StockUnitSimple();
        stockUnit.setId(productStockUnit.getStockUnit().getId());
        stockUnit.setName(productStockUnit.getStockUnit().getName());
        ProductStockUnitSimple simple = new ProductStockUnitSimple(product, stockUnit, productStockUnit.getPrice(),
                productStockUnit.getFactor(), productStockUnit.getStatus());
        return simple;
    }
}
