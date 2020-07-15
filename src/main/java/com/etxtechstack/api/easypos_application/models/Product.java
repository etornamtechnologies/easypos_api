package com.etxtechstack.api.easypos_application.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class Product extends AuditModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_products_generator")
    @SequenceGenerator(
            name = "sq_products_generator",
            sequenceName = "sq_products",
            allocationSize = 1
    )
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, unique = false)
    private String name;

    @Column(name = "barcode", nullable = true, unique = true)
    private String barcode;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "color", nullable = true)
    private String color;

    @Column(name = "size", nullable = true)
    private String size;

    @Column(name = "image_url", nullable = true)
    private String imageUrl;

    @Column(name = "stock_quantity", nullable = false, columnDefinition = "INTEGER default 0")
    private Integer stockQuantity;

    @Column(name = "status", nullable = false)
    private String status;

    @ManyToOne(
            cascade = CascadeType.MERGE
    )
    @JoinColumn(name = "default_stock_unit")
    private StockUnit defaultStockUnit;

    @ManyToOne(
            cascade = CascadeType.MERGE,
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "product_category_id")
    private ProductCategory productCategory;

    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ProductStockUnit> productStockUnits;

    public Product() {

    }

    public Product(Integer id, String name, String barcode, String description, String color, String size,
                   String imageUrl, Integer stockQuantity, StockUnit defaultStockUnit, ProductCategory productCategory) {
        this.id = id;
        this.name = name;
        this.barcode = barcode;
        this.description = description;
        this.color = color;
        this.size = size;
        this.imageUrl = imageUrl;
        this.stockQuantity = stockQuantity;
        this.defaultStockUnit = defaultStockUnit;
        this.productCategory = productCategory;
    }

    public Product(String name, String barcode, String description, String color, String size,
                   String imageUrl, Integer stockQuantity, StockUnit defaultStockUnit, ProductCategory productCategory) {
        this.name = name;
        this.barcode = barcode;
        this.description = description;
        this.color = color;
        this.size = size;
        this.imageUrl = imageUrl;
        this.stockQuantity = stockQuantity;
        this.defaultStockUnit = defaultStockUnit;
        this.productCategory = productCategory;
    }

    public Product(String name, String barcode, String description, String color, String size,
                   String imageUrl, Integer stockQuantity, StockUnit defaultStockUnit, ProductCategory productCategory,
                   List<ProductStockUnit> productStockUnits) {
        this.name = name;
        this.barcode = barcode;
        this.description = description;
        this.color = color;
        this.size = size;
        this.imageUrl = imageUrl;
        this.stockQuantity = stockQuantity;
        this.defaultStockUnit = defaultStockUnit;
        this.productCategory = productCategory;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public StockUnit getDefaultStockUnit() {
        return defaultStockUnit;
    }

    public void setDefaultStockUnit(StockUnit defaultStockUnit) {
        this.defaultStockUnit = defaultStockUnit;
    }

    public List<ProductStockUnit> getProductStockUnits() {
        return productStockUnits;
    }

    public void setProductStockUnits(List<ProductStockUnit> productStockUnits) {
        this.productStockUnits = productStockUnits;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", barcode='" + barcode + '\'' +
                ", description='" + description + '\'' +
                ", color='" + color + '\'' +
                ", size='" + size + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", stockQuantity=" + stockQuantity +
                ", defaultStockUnit=" + defaultStockUnit.getName() +
                ", productCategory=" + productCategory.getName() +
                '}';
    }
}
