package com.etxtechstack.api.easypos_application.utils;

import com.etxtechstack.api.easypos_application.models.*;
import com.etxtechstack.api.easypos_application.repositories.*;
import com.etxtechstack.api.easypos_application.vo.DBValidationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DBValidateHelper {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StockUnitRepository stockUnitRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ProductRepository productRepository;

    public DBValidationResponse validateRole(String name) {
        boolean exists = false;
        String message = "User With ";

        try {
            Optional<Role> nameExists = roleRepository.findRoleByNameIgnoreCase(name);
            if(nameExists.isPresent()) {
                return new DBValidationResponse(true, "Role With Name (" + name + ") Already Exist", nameExists.get().getId());
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return new DBValidationResponse(false, "Does Not Exist");
    };

    public DBValidationResponse validateUser(String username, String email, String phone) {
        boolean exists = false;
        String message = "User With ";
        Optional<User> emailExists = null;
        Optional<User> phoneExists = null;
        Optional<User> usernameExists = null;
        try {
            emailExists = userRepository.findUserByEmailIgnoreCase(email);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        if(emailExists.isPresent()) {
            exists = true;
            message += "Email Already Exists";
            return new DBValidationResponse(exists, message, emailExists.get().getId());
        }
        try {
            phoneExists = userRepository.findUserByPhone(phone);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        if(phoneExists.isPresent()) {
            exists = true;
            message += "Phone Number Already Exists";
            return new DBValidationResponse(exists, message, phoneExists.get().getId());
        }
        try {
            usernameExists = userRepository.findUserByUsernameIgnoreCase(username);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        if(usernameExists.isPresent()) {
            exists = true;
            message += "Username Already Exists";
            return new DBValidationResponse(exists, message, usernameExists.get().getId());
        }
        return new DBValidationResponse(exists, message);
    }

    public DBValidationResponse validateStockUnit(String name) {
        boolean exists = false;
        String message = "Stock Unit With ";
        try {
            Optional<StockUnit> nameExists = stockUnitRepository.findStockUnitByNameIgnoreCase(name);
            if(nameExists.isPresent()) {
                return new DBValidationResponse(true, "Stock Unit With Name (" + name + ") Already Exist", nameExists.get().getId());
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return new DBValidationResponse(false, "Does Not Exist");
    };

    public DBValidationResponse validateProductCategory(String name) {
        boolean exists = false;
        String message = "Stock Unit With ";
        try {
            Optional<ProductCategory> nameExists = productCategoryRepository.findProductCategoryByNameIgnoreCase(name);
            if(nameExists.isPresent()) {
                return new DBValidationResponse(true, "Product Category With Name (" + name + ") Already Exist", nameExists.get().getId());
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return new DBValidationResponse(false, "Does Not Exist");
    };

    public DBValidationResponse validateProduct(String name, String barcode) {
        boolean exists = false;
        String message = "Does Not Exist";
        Optional<Product> nameExists = null;
        Optional<Product> barcodeExists = null;
        try {
            nameExists = productRepository.findProductByNameIgnoreCase(name);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        if(nameExists.isPresent()) {
            exists = true;
            message += "Product With Name /" + name + "/ Already Exists";
            return new DBValidationResponse(exists, message, nameExists.get().getId());
        }
        try {
            barcodeExists = productRepository.findProductByBarcode(barcode);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        if(barcodeExists.isPresent()) {
            exists = true;
            message += "Product With Barcode /" + barcode +  "/ Already Exists";
            return new DBValidationResponse(exists, message, barcodeExists.get().getId());
        }
        return new DBValidationResponse(exists, message);
    }
}
