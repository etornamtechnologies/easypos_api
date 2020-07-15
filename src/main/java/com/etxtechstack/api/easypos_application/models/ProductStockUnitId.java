package com.etxtechstack.api.easypos_application.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@SuppressWarnings("serial")
@Embeddable
public class ProductStockUnitId implements Serializable {
    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "stock_unit_id")
    private Integer stockUnitId;

    public ProductStockUnitId() {
    }

    public ProductStockUnitId(Integer productId, Integer stockUnitId) {
        this.productId = productId;
        this.stockUnitId = stockUnitId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getStockUnitId() {
        return stockUnitId;
    }

    public void setStockUnitId(Integer stockUnitId) {
        this.stockUnitId = stockUnitId;
    }
}
