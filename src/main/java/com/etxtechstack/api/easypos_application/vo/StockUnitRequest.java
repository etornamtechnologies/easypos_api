package com.etxtechstack.api.easypos_application.vo;

public class StockUnitRequest {
    private Integer id;
    private String name;
    private Double factor;

    public StockUnitRequest() {
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

    public Double getFactor() {
        return factor;
    }

    public void setFactor(Double factor) {
        this.factor = factor;
    }
}
