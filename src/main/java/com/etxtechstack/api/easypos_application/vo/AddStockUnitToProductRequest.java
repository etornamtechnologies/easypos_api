package com.etxtechstack.api.easypos_application.vo;

public class AddStockUnitToProductRequest {
    private Integer stockUnitId;
    private Integer factor;
    private Double price;

    public AddStockUnitToProductRequest() {
    }

    public Integer getStockUnitId() {
        return stockUnitId;
    }

    public void setStockUnitId(Integer stockUnitId) {
        this.stockUnitId = stockUnitId;
    }

    public Integer getFactor() {
        return factor;
    }

    public void setFactor(Integer factor) {
        this.factor = factor;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
