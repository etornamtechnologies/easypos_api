package com.etxtechstack.api.easypos_application.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "sale_entries")
public class SaleEntry extends AuditModel{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_sale_entries_generator")
    @SequenceGenerator(
            name = "sq_sale_entries_generator",
            sequenceName = "sq_sale_entries",
            allocationSize = 1
    )
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @OneToOne(
            cascade = CascadeType.MERGE
    )
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @OneToOne(
            cascade = CascadeType.MERGE
    )
    @JoinColumn(name = "stock_unit_id", referencedColumnName = "id")
    private StockUnit stockUnit;

    @Column(name = "unit_price", nullable = false)
    private Integer unitPrice;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @JsonIgnore
    @ManyToOne(
            fetch = FetchType.EAGER,
            optional = true
    )
    @JoinColumn(name = "sale_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Sale sale;

    public SaleEntry() {
    }

    public SaleEntry(Product product, StockUnit stockUnit, Integer unitPrice, Integer quantity, Sale sale) {
        this.product = product;
        this.stockUnit = stockUnit;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.sale = sale;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    @Override
    public String toString() {
        return "SaleEntry{" +
                "id=" + id +
                ", product=" + product +
                ", stockUnit=" + stockUnit +
                ", quantity=" + quantity +
                ", sale=" + sale.getId() +
                '}';
    }
}
