package com.etxtechstack.api.easypos_application.vo;

import java.util.Map;

public class GeneralResponse extends BasicResponse{
    private Map data;

    public GeneralResponse() {

    }

    public GeneralResponse(Map data) {
        this.data = data;
    }

    public GeneralResponse(Integer code, String message, Map data) {
        super(code, message);
        this.data = data;
    }

    public GeneralResponse(Integer code, String message) {
        super(code, message);
    }

    public Map getData() {
        return data;
    }

    public void setData(Map data) {
        this.data = data;
    }


}
