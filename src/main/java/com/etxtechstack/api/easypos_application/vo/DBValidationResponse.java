package com.etxtechstack.api.easypos_application.vo;

public class DBValidationResponse {
    private boolean exists;
    private String message;
    private Integer id;

    public DBValidationResponse() {
    }

    public DBValidationResponse(boolean exists, String message) {
        this.exists = exists;
        this.message = message;
    }

    public DBValidationResponse(boolean exists, String message, Integer id) {
        this.exists = exists;
        this.message = message;
        this.id = id;
    }

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "DBValidateResponse{" +
                "exists=" + exists +
                ", message='" + message + '\'' +
                '}';
    }
}

