package com.etxtechstack.api.easypos_application.vo;

public class UpdateProductStockUnitRequestVo {
    private Double price;
    private String status;
    private Integer factor;

    public UpdateProductStockUnitRequestVo() {
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getFactor() {
        return factor;
    }

    public void setFactor(Integer factor) {
        this.factor = factor;
    }
}
