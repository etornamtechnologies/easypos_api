package com.etxtechstack.api.easypos_application.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stock_units")
public class StockUnit extends AuditModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_stock_units_generator")
    @SequenceGenerator(
            name = "sq_stock_units_generator",
            sequenceName = "sq_stock_units",
            allocationSize = 1
    )
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @JsonIgnore
    @OneToMany(
            mappedBy = "defaultStockUnit"
    )
    private List<Product> defaultProducts;

    @JsonIgnore
    @OneToMany(
            mappedBy = "stockUnit",
            cascade = CascadeType.MERGE
    )
    private List<ProductStockUnit> productStockUnits;

    public StockUnit() {
    }

    public StockUnit(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public StockUnit(String name) {
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

    public List<Product> getDefaultProducts() {
        return defaultProducts;
    }

    public void setDefaultProducts(List<Product> defaultProducts) {
        this.defaultProducts = defaultProducts;
    }

    @Override
    public String toString() {
        return "StockUnit{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
