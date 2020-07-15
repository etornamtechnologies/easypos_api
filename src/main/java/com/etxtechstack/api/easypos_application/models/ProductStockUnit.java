package com.etxtechstack.api.easypos_application.models;

import io.swagger.models.auth.In;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "ProductStockUnit")
@Table(name = "product_stock_unit")
public class ProductStockUnit implements Serializable {
    @EmbeddedId
    private ProductStockUnitId id;

    @ManyToOne
    @MapsId("productId")
    private Product product;

    @ManyToOne
    @MapsId("stockUnitId")
    private StockUnit stockUnit;

    @Column(name = "factor", nullable = false)
    private Integer factor;

    @Column(name = "price", nullable = false, columnDefinition = "INTEGER default 0")
    private Integer price;

    @Column(name = "status", nullable = false)
    private String status;

    public ProductStockUnit() {
    }

    public ProductStockUnit(Product product, StockUnit stockUnit) {
        this.product = product;
        this.stockUnit = stockUnit;
    }

    public ProductStockUnit(StockUnit stockUnit, Integer factor, Integer price) {
        this.factor = factor;
        this.price = price;
        this.stockUnit = stockUnit;
    }

    public ProductStockUnit(Product product, StockUnit stockUnit, Integer factor,Integer price) {
        this.product = product;
        this.stockUnit = stockUnit;
        this.factor = factor;
        this.price = price;
    }

    public ProductStockUnit(Product product, StockUnit stockUnit, Integer factor,Integer price, String status) {
        this.product = product;
        this.stockUnit = stockUnit;
        this.factor = factor;
        this.price = price;
        this.status = status;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public StockUnit getStockUnit() {
        return stockUnit;
    }

    public void setStockUnit(StockUnit stockUnit) {
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

    public ProductStockUnitId getId() {
        return id;
    }

    public void setId(ProductStockUnitId id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ProductStockUnit{" +
                "id=" + id +
                ", product=" + product +
                ", stockUnit=" + stockUnit +
                ", factor=" + factor +
                ", price=" + price +
                ", status='" + status + '\'' +
                '}';
    }
}
