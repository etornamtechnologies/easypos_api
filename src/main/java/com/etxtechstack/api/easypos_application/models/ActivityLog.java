package com.etxtechstack.api.easypos_application.models;

import javax.persistence.*;

@Entity
@Table(name = "activity_logs")
public class ActivityLog extends AuditModel{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_activity_logs_generator")
    @SequenceGenerator(
            name = "sq_activity_logs_generator",
            sequenceName = "sq_activity_logs",
            allocationSize = 1
    )
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @Column(name = "source_ip", nullable = false)
    private String sourceIp;

    @Column(name = "destination_ip", nullable = false)
    private String destinationIp;

    @Column(name = "event", nullable = false)
    private String event;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "entity_id", nullable = false)
    private String entityId;

    @Column(name = "entity_name", nullable = false)
    private String entityName;

    @Column(name = "json_data", nullable = true)
    private String jsonData;

    @ManyToOne(
            cascade = CascadeType.MERGE
    )
    @JoinColumn(name = "user_id")
    private User user;

    public ActivityLog() {
    }

    public ActivityLog(String event, String model, String entityId, String entityName, String jsonData, String sourceIp,
                       String destinationIp, User user) {
        this.event = event;
        this.model = model;
        this.entityId = entityId;
        this.entityName = entityName;
        this.jsonData = jsonData;
        this.destinationIp = destinationIp;
        this.sourceIp = sourceIp;
        this.user = user;
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

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "ActivityLog{" +
                "id=" + id +
                ", sourceIp='" + sourceIp + '\'' +
                ", destinationIp='" + destinationIp + '\'' +
                ", event='" + event + '\'' +
                ", model='" + model + '\'' +
                ", entityId='" + entityId + '\'' +
                ", entityName='" + entityName + '\'' +
                ", jsonData='" + jsonData + '\'' +
                ", user=" + user.getName() +
                '}';
    }
}
