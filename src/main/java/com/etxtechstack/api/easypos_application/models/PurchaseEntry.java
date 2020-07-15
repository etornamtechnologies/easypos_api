package com.etxtechstack.api.easypos_application.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "product_entries")
public class PurchaseEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_purchase_entries_generator")
    @SequenceGenerator(
            name = "sq_purchase_entries_generator",
            sequenceName = "sq_purchase_entries",
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
    @JoinColumn(name = "purchase_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Purchase purchase;

    public PurchaseEntry() {
    }

    public PurchaseEntry(Product product, StockUnit stockUnit, Integer unitPrice, Integer quantity, Purchase purchase) {
        this.product = product;
        this.stockUnit = stockUnit;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.purchase = purchase;
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

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    @Override
    public String toString() {
        return "PurchaseEntry{" +
                "id=" + id +
                ", product=" + product +
                ", stockUnit=" + stockUnit +
                ", unitPrice=" + unitPrice +
                ", quantity=" + quantity +
                ", purchase=" + purchase +
                '}';
    }
}
