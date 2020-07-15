package com.etxtechstack.api.easypos_application.vo;

public class ValidateTokenResponse extends BasicResponse{
    private Integer userId;

    public ValidateTokenResponse() {

    }

    public ValidateTokenResponse(Integer userId) {
        this.userId = userId;
    }

    public ValidateTokenResponse(Integer code, String message, Integer userId) {
        super(code, message);
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
