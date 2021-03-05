package com.etxtechstack.api.easypos_application.controller;

import com.etxtechstack.api.easypos_application.dos.ProductSimple;
import com.etxtechstack.api.easypos_application.models.*;
import com.etxtechstack.api.easypos_application.services.ActivityLogService;
import com.etxtechstack.api.easypos_application.services.InventoryService;
import com.etxtechstack.api.easypos_application.services.UserService;
import com.etxtechstack.api.easypos_application.utils.*;
import com.etxtechstack.api.easypos_application.vo.*;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/etx-easypos/api/v1/inventory")
public class InventoryController {
    private Gson gson;

    @Autowired
    private Logger logger;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ActivityLogService activityLogService;

    @Autowired
    private DBValidateHelper dbValidateHelper;

    public InventoryController() {
        gson = new Gson();
    }

    //==============STOCK UNITS ==========================
    @GetMapping(value = "/stock-units", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    GeneralResponse getAllStockUnits(@RequestHeader(name = "Authorization", required = true) String token) {
        //lets authenticate
        ValidateTokenResponse validateTokenResponse = jwtUtil.validateUserToken(token);
        if(!validateTokenResponse.getCode().equals(CommonResponse.SUCCESS_CODE)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, CommonResponse.INVALID_JWT_TOKEN);
        }
        Integer authUserId = validateTokenResponse.getUserId();
        User authUser = userService.getUserById(authUserId);
        //lets authorize action
        if(!CommonHelper.HasPermission(Permissions.StockUnitGetAll, authUser.getRole().getPermissions())) {
            return new GeneralResponse(CommonResponse.INSUFFICIENT_PERMISSION_CODE, CommonResponse.INSUFFICIENT_PERMISSION_MSG);
        }
        List<StockUnit> stockUnits = inventoryService.getAllStockUnits();
        Map data = new HashMap();
        data.put("stockUnits", stockUnits);
        return new GeneralResponse(CommonResponse.SUCCESS_CODE, CommonResponse.SUCCESS_MSG, data);
    }

    @Transactional
    @PostMapping(value = "/stock-units", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    GeneralResponse createStockUnit(@RequestHeader(name = "Authorization", required = true) String token,
                                    @RequestBody StockUnitRequest reqBody,
                                    HttpServletRequest request) {
        //lets authenticate
        ValidateTokenResponse validateTokenResponse = jwtUtil.validateUserToken(token);
        if(!validateTokenResponse.getCode().equals(CommonResponse.SUCCESS_CODE)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, CommonResponse.INVALID_JWT_TOKEN);
        }
        Integer authUserId = validateTokenResponse.getUserId();
        User authUser = userService.getUserById(authUserId);
        //lets authorize action
        if(!CommonHelper.HasPermission(Permissions.StockUnitCreate, authUser.getRole().getPermissions())) {
            return new GeneralResponse(CommonResponse.INSUFFICIENT_PERMISSION_CODE, CommonResponse.INSUFFICIENT_PERMISSION_MSG);
        }
        if(reqBody.getName().isEmpty()) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, "NAME FIELD CANNOT BE EMPTY!");
        } else if(reqBody.getFactor() == null) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, "SCALE FACTOR FIELD CANNOT BE EMPTY!");
        }
        DBValidationResponse validateRes = dbValidateHelper.validateStockUnit(reqBody.getName());
        if(validateRes.isExists()) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, validateRes.getMessage());
        }
        StockUnit stockUnitModel = new StockUnit(reqBody.getName().toUpperCase());
        StockUnit createdStockUnit = inventoryService.saveStockUnit(stockUnitModel);
        //lets log action
        ActivityLog logModel = new ActivityLog(SystemEvents.CREATE, SystemModels.STOCK_UNIT, String.valueOf(createdStockUnit.getId()),
                createdStockUnit.getName(), gson.toJson(reqBody), request.getRemoteAddr(), request.getLocalAddr(), authUser);
        activityLogService.createActivityLog(logModel);
        Map data = new HashMap();
        data.put("stockUnit", createdStockUnit);
        return new GeneralResponse(CommonResponse.SUCCESS_CODE, CommonResponse.SUCCESS_MSG, data);
    }

    @Transactional
    @PutMapping(value = "/stock-units/{stockUnitId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    GeneralResponse updateStockUnit(@RequestHeader(name = "Authorization", required = true) String token,
                                    @PathVariable(name = "stockUnitId") Integer stockUnitId,
                                    @RequestBody StockUnitRequest reqBody,
                                    HttpServletRequest request) {
        //lets authenticate
        ValidateTokenResponse validateTokenResponse = jwtUtil.validateUserToken(token);
        if(!validateTokenResponse.getCode().equals(CommonResponse.SUCCESS_CODE)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, CommonResponse.INVALID_JWT_TOKEN);
        }
        Integer authUserId = validateTokenResponse.getUserId();
        User authUser = userService.getUserById(authUserId);
        //lets authorize action
        if(!CommonHelper.HasPermission(Permissions.StockUnitUpdate, authUser.getRole().getPermissions())) {
            return new GeneralResponse(CommonResponse.INSUFFICIENT_PERMISSION_CODE, CommonResponse.INSUFFICIENT_PERMISSION_MSG);
        }
        if(reqBody.getName().isEmpty()) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, "NAME FIELD CANNOT BE EMPTY!");
        } else if(reqBody.getFactor() == null) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, "SCALE FACTOR FIELD CANNOT BE EMPTY!");
        }
        DBValidationResponse validateRes = dbValidateHelper.validateStockUnit(reqBody.getName());
        if(validateRes.isExists() && !validateRes.getId().equals(stockUnitId)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, validateRes.getMessage());
        }
        StockUnit stockUnitModel = inventoryService.getStockUnitById(stockUnitId);
        stockUnitModel.setName(reqBody.getName().toUpperCase());
        StockUnit updatedStockUnit = inventoryService.saveStockUnit(stockUnitModel);
        //lets log action
        ActivityLog logModel = new ActivityLog(SystemEvents.UPDATE, SystemModels.STOCK_UNIT, String.valueOf(updatedStockUnit.getId()),
                updatedStockUnit.getName(), gson.toJson(reqBody), request.getRemoteAddr(), request.getLocalAddr(), authUser);
        activityLogService.createActivityLog(logModel);
        Map data = new HashMap();
        data.put("stockUnit", updatedStockUnit);
        return new GeneralResponse(CommonResponse.SUCCESS_CODE, CommonResponse.SUCCESS_MSG, data);
    }

    @Transactional
    @DeleteMapping(value = "/stock-units/{stockUnitId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    GeneralResponse deleteStockUnit(@RequestHeader(name = "Authorization", required = true) String token,
                                    @PathVariable(name = "stockUnitId") Integer stockUnitId,
                                    HttpServletRequest request) {
        //lets authenticate
        ValidateTokenResponse validateTokenResponse = jwtUtil.validateUserToken(token);
        if(!validateTokenResponse.getCode().equals(CommonResponse.SUCCESS_CODE)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, CommonResponse.INVALID_JWT_TOKEN);
        }
        Integer authUserId = validateTokenResponse.getUserId();
        User authUser = userService.getUserById(authUserId);
        //lets authorize action
        if(!CommonHelper.HasPermission(Permissions.StockUnitDelete, authUser.getRole().getPermissions())) {
            return new GeneralResponse(CommonResponse.INSUFFICIENT_PERMISSION_CODE, CommonResponse.INSUFFICIENT_PERMISSION_MSG);
        }
        StockUnit stockUnitModel = inventoryService.getStockUnitById(stockUnitId);
        inventoryService.deleteStockUnit(stockUnitModel);
        //lets log action
        ActivityLog logModel = new ActivityLog(SystemEvents.DELETE, SystemModels.STOCK_UNIT, String.valueOf(stockUnitModel.getId()),
                stockUnitModel.getName(), null, request.getRemoteAddr(), request.getLocalAddr(), authUser);
        activityLogService.createActivityLog(logModel);
        return new GeneralResponse(CommonResponse.SUCCESS_CODE, CommonResponse.SUCCESS_MSG);
    }

    @GetMapping(value = "/stock-units/{stockUnitId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    GeneralResponse getStockUnit(@RequestHeader(name = "Authorization", required = true) String token,
                                    @PathVariable(name = "stockUnitId") Integer stockUnitId,
                                    HttpServletRequest request) {
        //lets authenticate
        ValidateTokenResponse validateTokenResponse = jwtUtil.validateUserToken(token);
        if(!validateTokenResponse.getCode().equals(CommonResponse.SUCCESS_CODE)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, CommonResponse.INVALID_JWT_TOKEN);
        }
        Integer authUserId = validateTokenResponse.getUserId();
        User authUser = userService.getUserById(authUserId);
        Map data = new HashMap();
        //lets authorize action
        if(!CommonHelper.HasPermission(Permissions.StockUnitDelete, authUser.getRole().getPermissions())) {
            return new GeneralResponse(CommonResponse.INSUFFICIENT_PERMISSION_CODE, CommonResponse.INSUFFICIENT_PERMISSION_MSG);
        }
        StockUnit stockUnitModel = inventoryService.getStockUnitById(stockUnitId);
        data.put("stockUnit", stockUnitModel);
        //lets log action
        ActivityLog logModel = new ActivityLog(SystemEvents.DELETE, SystemModels.STOCK_UNIT, String.valueOf(stockUnitModel.getId()),
                stockUnitModel.getName(), null, request.getRemoteAddr(), request.getLocalAddr(), authUser);
        activityLogService.createActivityLog(logModel);
        return new GeneralResponse(CommonResponse.SUCCESS_CODE, CommonResponse.SUCCESS_MSG, data);
    }



    //=======================PRODUCT CATEGORIES===================

    @GetMapping(value = "/product-categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    GeneralResponse getAllProductCategories(@RequestHeader(name = "Authorization", required = true) String token) {
        //lets authenticate
        ValidateTokenResponse validateTokenResponse = jwtUtil.validateUserToken(token);
        if(!validateTokenResponse.getCode().equals(CommonResponse.SUCCESS_CODE)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, CommonResponse.INVALID_JWT_TOKEN);
        }
        Integer authUserId = validateTokenResponse.getUserId();
        User authUser = userService.getUserById(authUserId);
        //lets authorize action
        if(!CommonHelper.HasPermission(Permissions.ProductCategoryGetAll, authUser.getRole().getPermissions())) {
            return new GeneralResponse(CommonResponse.INSUFFICIENT_PERMISSION_CODE, CommonResponse.INSUFFICIENT_PERMISSION_MSG);
        }
        List<ProductCategory> productCategories = inventoryService.getAllProductCategories();
        Map data = new HashMap();
        data.put("productCategories", productCategories);
        return new GeneralResponse(CommonResponse.SUCCESS_CODE, CommonResponse.SUCCESS_MSG, data);
    }

    @Transactional
    @PostMapping(value = "/product-categories", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    GeneralResponse createProductCategory(@RequestHeader(name = "Authorization", required = true) String token,
                                    @RequestBody ProductCategoryRequest reqBody,
                                    HttpServletRequest request) {
        //lets authenticate
        ValidateTokenResponse validateTokenResponse = jwtUtil.validateUserToken(token);
        if(!validateTokenResponse.getCode().equals(CommonResponse.SUCCESS_CODE)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, CommonResponse.INVALID_JWT_TOKEN);
        }
        Integer authUserId = validateTokenResponse.getUserId();
        User authUser = userService.getUserById(authUserId);
        //lets authorize action
        if(!CommonHelper.HasPermission(Permissions.ProductCategoryCreate, authUser.getRole().getPermissions())) {
            return new GeneralResponse(CommonResponse.INSUFFICIENT_PERMISSION_CODE, CommonResponse.INSUFFICIENT_PERMISSION_MSG);
        }
        if(reqBody.getName().isEmpty()) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, "NAME FIELD CANNOT BE EMPTY!");
        }
        DBValidationResponse validateRes = dbValidateHelper.validateProductCategory(reqBody.getName());
        if(validateRes.isExists()) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, validateRes.getMessage());
        }
        ProductCategory productCategoryModel = new ProductCategory(reqBody.getName().toUpperCase(), reqBody.getDescription().toUpperCase());
        ProductCategory createdProductCategory = inventoryService.saveProductCategory(productCategoryModel);
        //lets log action
        ActivityLog logModel = new ActivityLog(SystemEvents.CREATE, SystemModels.PRODUCT_CATEGORY, String.valueOf(createdProductCategory.getId()),
                createdProductCategory.getName(), gson.toJson(reqBody), request.getRemoteAddr(), request.getLocalAddr(), authUser);
        activityLogService.createActivityLog(logModel);
        Map data = new HashMap();
        data.put("productCategory", createdProductCategory);
        return new GeneralResponse(CommonResponse.SUCCESS_CODE, CommonResponse.SUCCESS_MSG, data);
    }

    @Transactional
    @PutMapping(value = "/product-categories/{productCategoryId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    GeneralResponse updateProductCategory(@RequestHeader(name = "Authorization", required = true) String token,
                                    @PathVariable(name = "productCategoryId") Integer productCategoryId,
                                    @RequestBody ProductCategoryRequest reqBody,
                                    HttpServletRequest request) {
        //lets authenticate
        ValidateTokenResponse validateTokenResponse = jwtUtil.validateUserToken(token);
        if(!validateTokenResponse.getCode().equals(CommonResponse.SUCCESS_CODE)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, CommonResponse.INVALID_JWT_TOKEN);
        }
        Integer authUserId = validateTokenResponse.getUserId();
        User authUser = userService.getUserById(authUserId);
        //lets authorize action
        if(!CommonHelper.HasPermission(Permissions.ProductCategoryUpdate, authUser.getRole().getPermissions())) {
            return new GeneralResponse(CommonResponse.INSUFFICIENT_PERMISSION_CODE, CommonResponse.INSUFFICIENT_PERMISSION_MSG);
        }
        if(reqBody.getName().isEmpty()) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, "NAME FIELD CANNOT BE EMPTY!");
        }
        DBValidationResponse validateRes = dbValidateHelper.validateProductCategory(reqBody.getName());
        if(validateRes.isExists() && !validateRes.getId().equals(productCategoryId)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, validateRes.getMessage());
        }
        ProductCategory productCategoryModel = inventoryService.getProductCategoryById(productCategoryId);
        productCategoryModel.setName(reqBody.getName().toUpperCase());
        productCategoryModel.setDescription(reqBody.getDescription().toUpperCase());
        ProductCategory updatedProductCategory = inventoryService.saveProductCategory(productCategoryModel);
        //lets log action
        ActivityLog logModel = new ActivityLog(SystemEvents.UPDATE, SystemModels.PRODUCT_CATEGORY, String.valueOf(updatedProductCategory.getId()),
                updatedProductCategory.getName(), gson.toJson(reqBody), request.getRemoteAddr(), request.getLocalAddr(), authUser);
        activityLogService.createActivityLog(logModel);
        Map data = new HashMap();
        data.put("productCategory", updatedProductCategory);
        return new GeneralResponse(CommonResponse.SUCCESS_CODE, CommonResponse.SUCCESS_MSG, data);
    }

    @Transactional
    @DeleteMapping(value = "/product-categories/{productCategoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    GeneralResponse deleteProductCategory(@RequestHeader(name = "Authorization", required = true) String token,
                                    @PathVariable(name = "productCategoryId") Integer productCategoryId,
                                    HttpServletRequest request) {
        //lets authenticate
        ValidateTokenResponse validateTokenResponse = jwtUtil.validateUserToken(token);
        if(!validateTokenResponse.getCode().equals(CommonResponse.SUCCESS_CODE)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, CommonResponse.INVALID_JWT_TOKEN);
        }
        Integer authUserId = validateTokenResponse.getUserId();
        User authUser = userService.getUserById(authUserId);
        //lets authorize action
        if(!CommonHelper.HasPermission(Permissions.ProductCategoryDelete, authUser.getRole().getPermissions())) {
            return new GeneralResponse(CommonResponse.INSUFFICIENT_PERMISSION_CODE, CommonResponse.INSUFFICIENT_PERMISSION_MSG);
        }
        ProductCategory productCategoryModel = inventoryService.getProductCategoryById(productCategoryId);
        inventoryService.deleteProductCategory(productCategoryModel);
        //lets log action
        ActivityLog logModel = new ActivityLog(SystemEvents.DELETE, SystemModels.PRODUCT_CATEGORY, String.valueOf(productCategoryModel.getId()),
                productCategoryModel.getName(), null, request.getRemoteAddr(), request.getLocalAddr(), authUser);
        activityLogService.createActivityLog(logModel);
        return new GeneralResponse(CommonResponse.SUCCESS_CODE, CommonResponse.SUCCESS_MSG);
    }

    @Transactional
    @GetMapping(value = "/product-categories/{productCategoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    GeneralResponse getProductCategory(@RequestHeader(name = "Authorization", required = true) String token,
                                          @PathVariable(name = "productCategoryId") Integer productCategoryId,
                                          HttpServletRequest request) {
        //lets authenticate
        ValidateTokenResponse validateTokenResponse = jwtUtil.validateUserToken(token);
        Map data = new HashMap();
        if(!validateTokenResponse.getCode().equals(CommonResponse.SUCCESS_CODE)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, CommonResponse.INVALID_JWT_TOKEN);
        }
        Integer authUserId = validateTokenResponse.getUserId();
        User authUser = userService.getUserById(authUserId);
        //lets authorize action
        if(!CommonHelper.HasPermission(Permissions.ProductCategoryDelete, authUser.getRole().getPermissions())) {
            return new GeneralResponse(CommonResponse.INSUFFICIENT_PERMISSION_CODE, CommonResponse.INSUFFICIENT_PERMISSION_MSG);
        }
        ProductCategory productCategoryModel = inventoryService.getProductCategoryById(productCategoryId);
        data.put("productCategory", productCategoryModel);
        //lets log action
        ActivityLog logModel = new ActivityLog(SystemEvents.DELETE, SystemModels.PRODUCT_CATEGORY, String.valueOf(productCategoryModel.getId()),
                productCategoryModel.getName(), null, request.getRemoteAddr(), request.getLocalAddr(), authUser);
        activityLogService.createActivityLog(logModel);
        return new GeneralResponse(CommonResponse.SUCCESS_CODE, CommonResponse.SUCCESS_MSG, data);
    }


    //=======================PRODUCT===================

    @GetMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    GeneralResponse getAllProducts(@RequestHeader(name = "Authorization", required = true) String token,
                                   @RequestParam(name = "filter", required = false) String filter) {
        //lets authenticate
        ValidateTokenResponse validateTokenResponse = jwtUtil.validateUserToken(token);
        if(!validateTokenResponse.getCode().equals(CommonResponse.SUCCESS_CODE)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, CommonResponse.INVALID_JWT_TOKEN);
        }
        Integer authUserId = validateTokenResponse.getUserId();
        User authUser = userService.getUserById(authUserId);
        //lets authorize action
        if(!CommonHelper.HasPermission(Permissions.ProductGetAll, authUser.getRole().getPermissions())) {
            return new GeneralResponse(CommonResponse.INSUFFICIENT_PERMISSION_CODE, CommonResponse.INSUFFICIENT_PERMISSION_MSG);
        }
        List<Product> products = inventoryService.getAllProducts();
        List<ProductSimple> productList = products.stream().map(product-> {
            return MapperUtil.MapProductToProductSimple(product);
        }).collect(Collectors.toList());
        Map data = new HashMap();
        data.put("products", productList);
        return new GeneralResponse(CommonResponse.SUCCESS_CODE, CommonResponse.SUCCESS_MSG, data);
    }

    @GetMapping(value = "/products/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    GeneralResponse getProductDetails(@RequestHeader(name = "Authorization", required = true) String token,
                                      @PathVariable(name = "productId") Integer productId) {
        //lets authenticate
        ValidateTokenResponse validateTokenResponse = jwtUtil.validateUserToken(token);
        if(!validateTokenResponse.getCode().equals(CommonResponse.SUCCESS_CODE)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, CommonResponse.INVALID_JWT_TOKEN);
        }
        Integer authUserId = validateTokenResponse.getUserId();
        User authUser = userService.getUserById(authUserId);
        //lets authorize action
        if(!CommonHelper.HasPermission(Permissions.ProductGetAll, authUser.getRole().getPermissions())) {
            return new GeneralResponse(CommonResponse.INSUFFICIENT_PERMISSION_CODE, CommonResponse.INSUFFICIENT_PERMISSION_MSG);
        }
        Product product = inventoryService.getProductById(productId);
        ProductSimple productData = MapperUtil.MapProductToProductSimple(product);
        Map data = new HashMap();
        data.put("product", productData);
        return new GeneralResponse(CommonResponse.SUCCESS_CODE, CommonResponse.SUCCESS_MSG, data);
    }

    @Transactional
    @PostMapping(value = "/products", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    GeneralResponse createProduct(@RequestHeader(name = "Authorization", required = true) String token,
                                  @RequestBody ProductRequestVo reqBody,
                                  HttpServletRequest request) {
        //lets authenticate
        ValidateTokenResponse validateTokenResponse = jwtUtil.validateUserToken(token);
        if(!validateTokenResponse.getCode().equals(CommonResponse.SUCCESS_CODE)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, CommonResponse.INVALID_JWT_TOKEN);
        }
        Integer authUserId = validateTokenResponse.getUserId();
        User authUser = userService.getUserById(authUserId);
        //lets authorize action
        if(!CommonHelper.HasPermission(Permissions.ProductCategoryCreate, authUser.getRole().getPermissions())) {
            return new GeneralResponse(CommonResponse.INSUFFICIENT_PERMISSION_CODE, CommonResponse.INSUFFICIENT_PERMISSION_MSG);
        }
        //check for not null values
        if(reqBody.getName().isEmpty()) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, "PRODUCT NAME FIELD CANNOT BE EMPTY!");
        } else if(reqBody.getProductCategoryId() == null) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, "PRODUCT CATEGORY FIELD CANNOT BE EMPTY!");
        } else if(reqBody.getDefaultStockUnitId() == null) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, "PRODUCT NAME FIELD CANNOT BE EMPTY!");
        }
        DBValidationResponse validateRes = dbValidateHelper.validateProduct(reqBody.getName(), reqBody.getBarcode());
        if(validateRes.isExists()) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, validateRes.getMessage());
        }
        StockUnit defaultStockUnit = inventoryService.getStockUnitById(reqBody.getDefaultStockUnitId());
        ProductCategory productCategory = inventoryService.getProductCategoryById(reqBody.getProductCategoryId());
        Product productModel = new Product(reqBody.getName().toUpperCase(), reqBody.getBarcode(), reqBody.getDescription(),
                reqBody.getColor(), reqBody.getSize(), null, reqBody.getStockQuantity(), defaultStockUnit, productCategory);
        Product createdProduct = inventoryService.createProduct(productModel);
        ProductStockUnit productStockUnit = new ProductStockUnit(productModel, defaultStockUnit, 1, 0, "Y");
        inventoryService.addStockUnitToProduct(productStockUnit);
        //lets save product to db

        //lets log action
        ActivityLog logModel = new ActivityLog(SystemEvents.CREATE, SystemModels.PRODUCT, String.valueOf(createdProduct.getId()),
                createdProduct.getName(), gson.toJson(reqBody), request.getRemoteAddr(), request.getLocalAddr(), authUser);
        activityLogService.createActivityLog(logModel);
        Map data = new HashMap();
        data.put("product", inventoryService.getProductById(createdProduct.getId()));
        return new GeneralResponse(CommonResponse.SUCCESS_CODE, CommonResponse.SUCCESS_MSG, data);
    }

    @PutMapping(value = "/products/{productId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    GeneralResponse updateProduct(@RequestHeader(name = "Authorization", required = true) String token,
                                    @PathVariable(name = "productId") Integer productId,
                                    @RequestBody ProductRequestVo reqBody,
                                    HttpServletRequest request) {
        //lets authenticate
        ValidateTokenResponse validateTokenResponse = jwtUtil.validateUserToken(token);
        if(!validateTokenResponse.getCode().equals(CommonResponse.SUCCESS_CODE)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, CommonResponse.INVALID_JWT_TOKEN);
        }
        if(reqBody.getName().isEmpty()) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, "PRODUCT NAME FIELD CANNOT BE EMPTY!");
        } else if(reqBody.getProductCategoryId() == null) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, "PRODUCT CATEGORY FIELD CANNOT BE EMPTY!");
        } else if(reqBody.getDefaultStockUnitId() == null) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, "PRODUCT NAME FIELD CANNOT BE EMPTY!");
        }
        Integer authUserId = validateTokenResponse.getUserId();
        User authUser = userService.getUserById(authUserId);
        //lets authorize action
        if(!CommonHelper.HasPermission(Permissions.ProductUpdate, authUser.getRole().getPermissions())) {
            return new GeneralResponse(CommonResponse.INSUFFICIENT_PERMISSION_CODE, CommonResponse.INSUFFICIENT_PERMISSION_MSG);
        }
        DBValidationResponse validateRes = dbValidateHelper.validateProduct(reqBody.getName(), reqBody.getBarcode());
        if(validateRes.isExists() && !productId.equals(validateRes.getId())) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, validateRes.getMessage());
        }
        ProductCategory productCategory = inventoryService.getProductCategoryById(reqBody.getProductCategoryId());
        Product productModel = inventoryService.getProductById(productId);
        productModel.setName(reqBody.getName().toUpperCase());
        productModel.setDescription(reqBody.getDescription().toUpperCase());
        productModel.setBarcode(reqBody.getBarcode());
        productModel.setColor(reqBody.getColor());
        productModel.setSize(reqBody.getSize());
        productModel.setProductCategory(productCategory);
        Product updatedProduct = inventoryService.saveProduct(productModel);
        //lets log action
        ActivityLog logModel = new ActivityLog(SystemEvents.UPDATE, SystemModels.PRODUCT, String.valueOf(updatedProduct.getId()),
                updatedProduct.getName(), gson.toJson(reqBody), request.getRemoteAddr(), request.getLocalAddr(), authUser);
        activityLogService.createActivityLog(logModel);
        Map data = new HashMap();
        data.put("product", MapperUtil.MapProductToProductSimple(updatedProduct));
        return new GeneralResponse(CommonResponse.SUCCESS_CODE, CommonResponse.SUCCESS_MSG, data);
    }

    @DeleteMapping(value = "/products/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    GeneralResponse deleteProduct(@RequestHeader(name = "Authorization", required = true) String token,
                                          @PathVariable(name = "productId") Integer productId,
                                          HttpServletRequest request) {
        //lets authenticate
        ValidateTokenResponse validateTokenResponse = jwtUtil.validateUserToken(token);
        if(!validateTokenResponse.getCode().equals(CommonResponse.SUCCESS_CODE)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, CommonResponse.INVALID_JWT_TOKEN);
        }
        Integer authUserId = validateTokenResponse.getUserId();
        User authUser = userService.getUserById(authUserId);
        //lets authorize action
        if(!CommonHelper.HasPermission(Permissions.ProductDelete, authUser.getRole().getPermissions())) {
            return new GeneralResponse(CommonResponse.INSUFFICIENT_PERMISSION_CODE, CommonResponse.INSUFFICIENT_PERMISSION_MSG);
        }
        Product productModel = inventoryService.getProductById(productId);
        inventoryService.deleteProduct(productModel);
        //lets log action
        ActivityLog logModel = new ActivityLog(SystemEvents.DELETE, SystemModels.PRODUCT, String.valueOf(productModel.getId()),
                productModel.getName(), null, request.getRemoteAddr(), request.getLocalAddr(), authUser);
        activityLogService.createActivityLog(logModel);
        return new GeneralResponse(CommonResponse.SUCCESS_CODE, CommonResponse.SUCCESS_MSG);
    }

    @Transactional
    @PutMapping(value = "/products/{productId}/stock-units/{stockUnitId}")
    public @ResponseBody
    GeneralResponse updateProductStockUnit(@RequestHeader(name = "Authorization") String token,
                                           @PathVariable(name = "productId") Integer productId,
                                           @PathVariable(name = "stockUnitId") Integer stockUnitId,
                                           @RequestBody UpdateProductStockUnitRequestVo reqBody) {
        logger.info("=============In Update Product Stock Unit=====================");
        //lets authenticate
        ValidateTokenResponse validateTokenResponse = jwtUtil.validateUserToken(token);
        if(!validateTokenResponse.getCode().equals(CommonResponse.SUCCESS_CODE)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, CommonResponse.INVALID_JWT_TOKEN);
        }
        Integer authUserId = validateTokenResponse.getUserId();
        User authUser = userService.getUserById(authUserId);
        //lets authorize action
        if(!CommonHelper.HasPermission(Permissions.ProductCreate, authUser.getRole().getPermissions())) {
            return new GeneralResponse(CommonResponse.INSUFFICIENT_PERMISSION_CODE, CommonResponse.INSUFFICIENT_PERMISSION_MSG);
        }
        Product product = inventoryService.getProductById(productId);
        StockUnit stockUnit = inventoryService.getStockUnitById(stockUnitId);
        ProductStockUnit productStockUnit = inventoryService.getProductStockUnitByProductAndStockUnit(product, stockUnit);
        ProductStockUnit model = new ProductStockUnit();
        if(productStockUnit.getFactor().equals(1) && !reqBody.getFactor().equals(1)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, "Error! You Cannot Change Default Stock Unit Factor!");
        }
        model.setFactor(reqBody.getFactor());
        model.setStatus(reqBody.getStatus());
        model.setPrice(CommonHelper.formatMoney(reqBody.getPrice()));
        model.setProduct(product);
        model.setStockUnit(stockUnit);
        inventoryService.updateProductStockUnit(model);
        //lets get updated product
        Product updatedProduct = inventoryService.getProductById(product.getId());
        ProductSimple productData = MapperUtil.MapProductToProductSimple(updatedProduct);
        Map data = new HashMap();
        data.put("product", productData);
        return new GeneralResponse(CommonResponse.SUCCESS_CODE, CommonResponse.SUCCESS_MSG, data);
    }

    @Transactional
    @PutMapping(value = "/products/{productId}/stock-units")
    public @ResponseBody
    GeneralResponse addStockUnitToProduct(@RequestHeader(name = "Authorization") String token,
                                           @PathVariable(name = "productId") Integer productId,
                                           @RequestBody AddStockUnitToProductRequest reqBody) {
        //lets authenticate
        //lets authenticate
        ValidateTokenResponse validateTokenResponse = jwtUtil.validateUserToken(token);
        if(!validateTokenResponse.getCode().equals(CommonResponse.SUCCESS_CODE)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, CommonResponse.INVALID_JWT_TOKEN);
        }
        Integer authUserId = validateTokenResponse.getUserId();
        User authUser = userService.getUserById(authUserId);
        //lets authorize action
        if(!CommonHelper.HasPermission(Permissions.ProductCreate, authUser.getRole().getPermissions())) {
            return new GeneralResponse(CommonResponse.INSUFFICIENT_PERMISSION_CODE, CommonResponse.INSUFFICIENT_PERMISSION_MSG);
        }
        Product product = inventoryService.getProductById(productId);
        StockUnit stockUnit = inventoryService.getStockUnitById(reqBody.getStockUnitId());
        List<ProductStockUnit> productUnitStockUnits = inventoryService.getProductUnitStockUnits(product);
        System.out.println("=============DEFAULT STOCK UNIT LEN: " + productUnitStockUnits.size());
        if(productUnitStockUnits.size() > 0 && reqBody.getFactor().equals(1.0)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE,
                    "You Cannot Add This Stock Unit Because Product Already Has Unit Stock Unit!");
        }
        ProductStockUnit productStockUnit = new ProductStockUnit(product, stockUnit, reqBody.getFactor(),
                CommonHelper.formatMoney(reqBody.getPrice()), "Y");
        inventoryService.addStockUnitToProduct(productStockUnit);
        Product updatedProduct = inventoryService.getProductById(productId);
        ProductSimple productData = MapperUtil.MapProductToProductSimple(updatedProduct);
        Map data = new HashMap();
        data.put("product", productData);
        return new GeneralResponse(CommonResponse.SUCCESS_CODE, CommonResponse.SUCCESS_MSG, data);
    }


    @Transactional
    @DeleteMapping(value = "/products/{productId}/stock-units/{stockUnitId}")
    public @ResponseBody
    GeneralResponse removeStockUnitFromProduct(@RequestHeader(name = "Authorization") String token,
                                                @PathVariable(name = "productId") Integer productId,
                                                @PathVariable(name = "stockUnitId") Integer stockUnitId) {
        //lets authenticate
        //lets authenticate
        logger.info("===============In Remove Stock Unit From Product=====================");
        ValidateTokenResponse validateTokenResponse = jwtUtil.validateUserToken(token);
        if(!validateTokenResponse.getCode().equals(CommonResponse.SUCCESS_CODE)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, CommonResponse.INVALID_JWT_TOKEN);
        }
        Integer authUserId = validateTokenResponse.getUserId();
        User authUser = userService.getUserById(authUserId);
        //lets authorize action
        if(!CommonHelper.HasPermission(Permissions.ProductCreate, authUser.getRole().getPermissions())) {
            return new GeneralResponse(CommonResponse.INSUFFICIENT_PERMISSION_CODE, CommonResponse.INSUFFICIENT_PERMISSION_MSG);
        }
        Product product = inventoryService.getProductById(productId);
        StockUnit stockUnit = inventoryService.getStockUnitById(stockUnitId);
        ProductStockUnit productStockUnit = inventoryService.getProductStockUnitByProductAndStockUnit(product, stockUnit);
        if(productStockUnit.getFactor().equals(1)) {
            return new GeneralResponse(CommonResponse.ERROR_CODE, "Error! You Cannot Remove Default Stock Unit!");
        }
        inventoryService.removeStockUnitFromProduct(productStockUnit);
        ProductSimple productData = MapperUtil.MapProductToProductSimple(inventoryService.getProductById(productId));
        Map data = new HashMap();
        data.put("product", productData);
        return new GeneralResponse(CommonResponse.SUCCESS_CODE, CommonResponse.SUCCESS_MSG, data);
    }
}
