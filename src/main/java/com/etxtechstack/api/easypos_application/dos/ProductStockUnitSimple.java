package com.etxtechstack.api.easypos_application.dos;

import com.etxtechstack.api.easypos_application.models.StockUnit;

public class ProductStockUnitSimple {
    private ProductSimple product;
    private StockUnitSimple stockUnit;
    private Integer price;
    private Integer factor;
    private String status;

    public ProductStockUnitSimple() {
    }

    public ProductStockUnitSimple(ProductSimple product, StockUnitSimple stockUnit, Integer price, Integer factor, String status) {
        this.product = product;
        this.stockUnit = stockUnit;
        this.price = price;
        this.factor = factor;
        this.status = status;
    }

    public ProductSimple getProduct() {
        return product;
    }

    public void setProduct(ProductSimple product) {
        this.product = product;
    }

    public StockUnitSimple getStockUnit() {
        return stockUnit;
    }

    public void setStockUnit(StockUnitSimple stockUnit) {
        this.stockUnit = stockUnit;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getFactor() {
        return factor;
    }

    public void setFactor(Integer factor) {
        this.factor = factor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
