package com.etxtechstack.api.easypos_application.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "login_histories")
public class LoginHistory extends AuditModel{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_login_histories_generator")
    @SequenceGenerator(
            name = "sq_login_histories_generator",
            sequenceName = "sq_login_histories",
            allocationSize = 1
    )
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @Column(name = "source_ip", nullable = false)
    private String sourceIp;

    @Column(name = "destination_ip")
    private String destinationIp;

    @Column(name = "status")
    private String status;

    @Column(name = "reason", nullable = true)
    private String reason;

    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = CascadeType.MERGE
    )
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    public User user;

    public LoginHistory() {
    }

    public LoginHistory(String sourceIp, String destinationIp, String status, String reason, User user) {
        this.sourceIp = sourceIp;
        this.destinationIp = destinationIp;
        this.status = status;
        this.user = user;
        this.reason = reason;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public String getDestinationIp() {
        return destinationIp;
    }

    public void setDestinationIp(String destinationIp) {
        this.destinationIp = destinationIp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
