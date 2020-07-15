package com.etxtechstack.api.easypos_application.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "sales")
public class Sale extends AuditModel{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_sales_generator")
    @SequenceGenerator(
            name = "sq_sales_generator",
            sequenceName = "sq_sales",
            allocationSize = 1
    )
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @Column(name = "reference_number", nullable = false, unique = true)
    private String referenceNumber;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "total_cost", nullable = false)
    private Integer totalCost;

    @Column(name = "amount_paid", nullable = false)
    private Integer amountPaid;

    @ManyToOne(
            fetch = FetchType.EAGER,
            optional = true
    )
    @JoinColumn(name = "customer_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Customer customer;

    @ManyToOne(
            fetch = FetchType.EAGER,
            optional = true
    )
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private User user;

    @OneToMany(
            mappedBy = "sale"
    )
    private List<SaleEntry> saleEntries;

    public Sale() {

    }

    public Sale(String referenceNumber, String status, Integer totalCost, Integer amountPaid,
                Customer customer, User user) {
        this.referenceNumber = referenceNumber;
        this.status = status;
        this.totalCost = totalCost;
        this.amountPaid = amountPaid;
        this.customer = customer;
        this.user = user;
    }

    public Sale(String referenceNumber, String status, Integer totalCost, Integer amountPaid,
                Customer customer, User user, List<SaleEntry> saleEntries) {
        this.referenceNumber = referenceNumber;
        this.status = status;
        this.totalCost = totalCost;
        this.amountPaid = amountPaid;
        this.customer = customer;
        this.user = user;
        this.saleEntries = saleEntries;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Integer totalCost) {
        this.totalCost = totalCost;
    }

    public Integer getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Integer amountPaid) {
        this.amountPaid = amountPaid;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<SaleEntry> getSaleEntries() {
        return saleEntries;
    }

    public void setSaleEntries(List<SaleEntry> saleEntries) {
        this.saleEntries = saleEntries;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", referenceNumber='" + referenceNumber + '\'' +
                ", status='" + status + '\'' +
                ", totalCost=" + totalCost +
                ", amountPaid=" + amountPaid +
                ", customer=" + customer.getId() +
                ", user=" + user.getId() +
                '}';
    }
}
