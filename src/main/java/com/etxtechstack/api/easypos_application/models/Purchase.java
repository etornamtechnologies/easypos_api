package com.etxtechstack.api.easypos_application.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "purchases")
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_purchases_generator")
    @SequenceGenerator(
            name = "sq_purchases_generator",
            sequenceName = "sq_purchases",
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
    private Supplier supplier;

    @ManyToOne(
            fetch = FetchType.EAGER,
            optional = true
    )
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private User user;

    @OneToMany(
            mappedBy = "purchase"
    )
    private List<PurchaseEntry> purchaseEntries;
}
