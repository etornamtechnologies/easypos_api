package com.etxtechstack.api.easypos_application.dos;

import com.etxtechstack.api.easypos_application.models.ProductStockUnit;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ProductSimple implements Serializable {
    private Integer id;
    private String name;
    private String barcode;
    private String size;
    private String color;
    private String description;
    private Date createdAt;
    private ProductCategorySimple productCategory;
    private StockUnitSimple defaultStockUnit;
    private List<ProductStockUnitSimple> productStockUnits;
    private Integer stockQuantity;

    public ProductSimple() {
    }

    public ProductSimple(Integer id, String name, String barcode, String size, String color, String description,
                         Date createdAt, ProductCategorySimple productCategory, StockUnitSimple defaultStockUnit) {
        this.id = id;
        this.name = name;
        this.barcode = barcode;
        this.size = size;
        this.color = color;
        this.description = description;
        this.createdAt = createdAt;
        this.productCategory = productCategory;
        this.defaultStockUnit = defaultStockUnit;
    }

    public ProductSimple(Integer id, String name, String barcode, String size, String color, String description,
                         Date createdAt, ProductCategorySimple productCategory, StockUnitSimple defaultStockUnit, Integer stockQuantity) {
        this.id = id;
        this.name = name;
        this.barcode = barcode;
        this.size = size;
        this.color = color;
        this.description = description;
        this.createdAt = createdAt;
        this.productCategory = productCategory;
        this.defaultStockUnit = defaultStockUnit;
        this.stockQuantity = stockQuantity;
    }

    public ProductSimple(Integer id, String name, String barcode, String size, String color, String description,
                         Date createdAt, ProductCategorySimple productCategory, StockUnitSimple defaultStockUnit, Integer stockQuantity,
                         List<ProductStockUnitSimple> productStockUnits) {
        this.id = id;
        this.name = name;
        this.barcode = barcode;
        this.size = size;
        this.color = color;
        this.description = description;
        this.createdAt = createdAt;
        this.productCategory = productCategory;
        this.defaultStockUnit = defaultStockUnit;
        this.stockQuantity = stockQuantity;
        this.productStockUnits = productStockUnits;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public ProductCategorySimple getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategorySimple productCategory) {
        this.productCategory = productCategory;
    }

    public StockUnitSimple getDefaultStockUnit() {
        return defaultStockUnit;
    }

    public void setDefaultStockUnit(StockUnitSimple defaultStockUnit) {
        this.defaultStockUnit = defaultStockUnit;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public List<ProductStockUnitSimple> getProductStockUnits() {
        return productStockUnits;
    }

    public void setProductStockUnits(List<ProductStockUnitSimple> productStockUnits) {
        this.productStockUnits = productStockUnits;
    }
}
