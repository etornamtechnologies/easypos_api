package com.etxtechstack.api.easypos_application.vo;

import com.etxtechstack.api.easypos_application.dos.SaleEntry;

import java.util.List;

public class CreateSaleRequestVo {
    private Integer customerId;
    private List<SaleEntry> saleEntries;
    private Double paidAmount;
    private Double totalCost;

    public CreateSaleRequestVo() {
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public List<SaleEntry> getSaleEntries() {
        return saleEntries;
    }

    public void setSaleEntries(List<SaleEntry> saleEntries) {
        this.saleEntries = saleEntries;
    }

    public Double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }
}
