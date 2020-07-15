package com.etxtechstack.api.easypos_application.services;

import com.etxtechstack.api.easypos_application.models.Sale;
import com.etxtechstack.api.easypos_application.models.SaleEntry;
import com.etxtechstack.api.easypos_application.repositories.SaleEntryRepository;
import com.etxtechstack.api.easypos_application.repositories.SaleRepository;
import com.etxtechstack.api.easypos_application.utils.CommonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PosService {
    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private SaleEntryRepository saleEntryRepository;



    public String getNextSaleReferenceForDate(Date date) {
        Integer saleCountForToday = saleRepository.getSaleCountByDate(date);
        Integer saleNumber = saleCountForToday + 1;
        System.out.println("========SALE COUNT: " + saleNumber);
        String ref = "SAL" + CommonHelper.FormatDateToPrettyDateString(date) + saleNumber;
        return ref;
    }

    public String getNextPurchaseReferenceForDate(Date date) {
        Integer saleCountForToday = saleRepository.getSaleCountByDate(date);
        Integer saleNumber = saleCountForToday + 1;
        System.out.println("========SALE COUNT: " + saleNumber);
        String ref = "PUR" + CommonHelper.FormatDateToPrettyDateString(date) + saleNumber;
        return ref;
    }

    public Sale saveSale(Sale saleModel) {
        try {
            return saleRepository.save(saleModel);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public SaleEntry createSaleEntry(SaleEntry saleEntryModel) {
        try {
            return saleEntryRepository.save(saleEntryModel);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
