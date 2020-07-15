package com.etxtechstack.api.easypos_application.services;

import com.etxtechstack.api.easypos_application.repositories.ProductStockUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductStockUnitService {
    @Autowired
    private ProductStockUnitRepository productStockUnitRepository;
}
