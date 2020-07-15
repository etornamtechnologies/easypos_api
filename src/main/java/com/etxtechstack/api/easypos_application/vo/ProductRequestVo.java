package com.etxtechstack.api.easypos_application.vo;

import com.etxtechstack.api.easypos_application.models.StockUnit;

import java.io.Serializable;

public class ProductRequestVo implements Serializable {
    private Integer id;
    private String name;
    private String barcode;
    private String description;
    private String size;
    private String color;
    private Integer defaultStockUnitId;
    private Integer stockQuantity;
    private Integer productCategoryId;

    public ProductRequestVo() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getDefaultStockUnitId() {
        return defaultStockUnitId;
    }

    public void setDefaultStockUnitId(Integer defaultStockUnitId) {
        this.defaultStockUnitId = defaultStockUnitId;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Integer getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(Integer productCategoryId) {
        this.productCategoryId = productCategoryId;
    }
}
