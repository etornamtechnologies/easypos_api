package com.etxtechstack.api.easypos_application.controller;

import com.etxtechstack.api.easypos_application.models.*;
import com.etxtechstack.api.easypos_application.services.CustomerService;
import com.etxtechstack.api.easypos_application.services.InventoryService;
import com.etxtechstack.api.easypos_application.services.PosService;
import com.etxtechstack.api.easypos_application.services.UserService;
import com.etxtechstack.api.easypos_application.utils.CommonHelper;
import com.etxtechstack.api.easypos_application.utils.CommonResponse;
import com.etxtechstack.api.easypos_application.utils.JwtUtil;
import com.etxtechstack.api.easypos_application.utils.Permissions;
import com.etxtechstack.api.easypos_application.vo.CreateSaleRequestVo;
import com.etxtechstack.api.easypos_application.vo.GeneralResponse;
import com.etxtechstack.api.easypos_application.vo.ValidateTokenResponse;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/etx-easypos/api/v1/pos")
public class PosController {
    private Gson gson;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private PosService posService;

    @Autowired
    private Logger logger;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserService userService;

    public PosController() {
        gson = new Gson();
    }

    @PostMapping(value = "/sales", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    GeneralResponse createSale(@RequestHeader(name = "Authorization", required = true) String token,
                               @RequestBody CreateSaleRequestVo reqBody,
                               HttpServletRequest request) {
        //lets authenticate
        ValidateTokenResponse validateTokenResponse = jwtUtil.validateUserToken(token);
        if(!validateTokenResponse.getCode().equals(CommonResponse.SUCCESS_CODE)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, CommonResponse.INVALID_JWT_TOKEN);
        }
        Integer authUserId = validateTokenResponse.getUserId();
        User authUser = userService.getUserById(authUserId);
        //lets authorize action
//        if(!CommonHelper.HasPermission(Permissions.CreateSale, authUser.getRole().getPermissions())) {
//            return new GeneralResponse(CommonResponse.INSUFFICIENT_PERMISSION_CODE, CommonResponse.INSUFFICIENT_PERMISSION_MSG);
//        }
        String reference = posService.getNextSaleReferenceForDate(new Date());
        Customer customer = customerService.getCustomerById(reqBody.getCustomerId());

        Sale saleModel = new Sale(reference, "Y", CommonHelper.formatMoney(reqBody.getTotalCost()),
                CommonHelper.formatMoney(reqBody.getPaidAmount()), customer, authUser);
        Sale createdSale = posService.saveSale(saleModel);
        //lets save the entries
        List<SaleEntry> saleEntries = reqBody.getSaleEntries().stream().map(saleEntry -> {
            Product product = inventoryService.getProductById(saleEntry.getProductId());
            StockUnit stockUnit = inventoryService.getStockUnitById(saleEntry.getStockUnitId());
            ProductStockUnit productStockUnit = inventoryService.getProductStockUnitByProductAndStockUnit(product, stockUnit);
            Integer unitQuantity = saleEntry.getQuantity() * productStockUnit.getFactor();
            SaleEntry sE = new SaleEntry();
            sE.setProduct(product);
            sE.setStockUnit(stockUnit);
            sE.setUnitPrice(CommonHelper.formatMoney(saleEntry.getUnitPrice()));
            sE.setQuantity(saleEntry.getQuantity());
            sE.setSale(createdSale);
            return sE;
        }).collect(Collectors.toList());
        saleEntries.stream().forEach(saleEntry -> {
            posService.createSaleEntry(saleEntry);
        });
        return new GeneralResponse(CommonResponse.SUCCESS_CODE, CommonResponse.SUCCESS_MSG);
    }
}
