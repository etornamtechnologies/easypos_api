package com.etxtechstack.api.easypos_application.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User extends AuditModel{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_users_generator")
    @SequenceGenerator(
            name = "sq_users_generator",
            sequenceName = "sq_users",
            allocationSize = 1
    )
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone", nullable = false, unique = true)
    private String phone;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "address", nullable = true)
    private String address;

    @Column(name = "status", nullable = false, columnDefinition = "VARCHAR default 'Y'")
    private String status;

    @Column(name = "is_default_password", nullable = false, columnDefinition = "VARCHAR default 'Y'")
    private String isDefaultPassword;

    @Column(name = "failed_login_count", nullable = false, columnDefinition = "INTEGER default 0")
    private Integer failedLoginCount;

    @ManyToOne(
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "role_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Role role;

    @JsonIgnore
    @OneToMany(
            mappedBy = "user"
    )
    private List<LoginHistory> loginHistories;

    @JsonIgnore
    @OneToMany(
            mappedBy = "user"
    )
    private List<Sale> sales;

    @JsonIgnore
    @Column(name = "created_by", nullable = true)
    private Integer createdBy;

    @JsonIgnore
    @OneToMany(
            mappedBy = "user"
    )
    private List<ActivityLog> activityLogs;

    public User() {
    }

    public User(String name, String username, String email, String phone, String address, String password, String status,
                Role role, Integer createdBy) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.password = password;
        this.status = status;
        this.role = role;
        this.createdBy = createdBy;
    }

    public User(String name, String username, String email, String phone, String address, String password, String status,
                Role role, String isDefaultPassword, Integer failedLoginCount, Integer createdBy) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.password = password;
        this.status = status;
        this.role = role;
        this.createdBy = createdBy;
        this.isDefaultPassword = isDefaultPassword;
        this.failedLoginCount = failedLoginCount;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public String getIsDefaultPassword() {
        return isDefaultPassword;
    }

    public void setIsDefaultPassword(String isDefaultPassword) {
        this.isDefaultPassword = isDefaultPassword;
    }

    public Integer getFailedLoginCount() {
        return failedLoginCount;
    }

    public void setFailedLoginCount(Integer failedLoginCount) {
        this.failedLoginCount = failedLoginCount;
    }

    public List<LoginHistory> getLoginHistories() {
        return loginHistories;
    }

    public void setLoginHistories(List<LoginHistory> loginHistories) {
        this.loginHistories = loginHistories;
    }

    public List<ActivityLog> getActivityLogs() {
        return activityLogs;
    }

    public void setActivityLogs(List<ActivityLog> activityLogs) {
        this.activityLogs = activityLogs;
    }

    public List<Sale> getSales() {
        return sales;
    }

    public void setSales(List<Sale> sales) {
        this.sales = sales;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", address='" + address + '\'' +
                ", role=" + role.getName() +
                '}';
    }
}
