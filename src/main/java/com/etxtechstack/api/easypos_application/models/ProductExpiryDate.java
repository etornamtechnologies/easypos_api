package com.etxtechstack.api.easypos_application.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "product_expiry_dates")
public class ProductExpiryDate extends AuditModel{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_product_expiry_dates_generator")
    @SequenceGenerator(
            name = "sq_product_expiry_dates_generator",
            sequenceName = "sq_product_expiry_dates",
            allocationSize = 1
    )
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @Column(name = "expiry_date", nullable = false)
    private Date expiryDate;

    @OneToOne(
            cascade = CascadeType.MERGE
    )
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    public ProductExpiryDate() {
    }

    public ProductExpiryDate(Product product, Date expiryDate, Integer quantity) {
        this.expiryDate = expiryDate;
        this.product = product;
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "ProductExpiryDate{" +
                "id=" + id +
                ", expiryDate=" + expiryDate +
                ", product=" + product.getId() +
                ", quantity=" + quantity +
                '}';
    }
}
