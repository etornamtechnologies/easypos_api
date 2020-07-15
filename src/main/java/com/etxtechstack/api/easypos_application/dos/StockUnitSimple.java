package com.etxtechstack.api.easypos_application.dos;

public class StockUnitSimple {
    private Integer id;
    private String name;

    public StockUnitSimple() {
    }

    public StockUnitSimple(Integer id, String name) {
        this.id = id;
        this.name = name;
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
}
