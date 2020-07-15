package com.etxtechstack.api.easypos_application.vo;

public class BasicResponse {
    private Integer code;
    private String message;

    public BasicResponse() {
    }

    public BasicResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
