package com.etxtechstack.api.easypos_application.services;

import com.etxtechstack.api.easypos_application.models.Customer;
import com.etxtechstack.api.easypos_application.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        try {
            return customerRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Customer getCustomerById(Integer customerId) {
        try {
            return customerRepository.findById(customerId).orElseThrow(()-> {
                return new RuntimeException("Customer Not Found!");
            });
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Customer saveCustomer(Customer model) {
        try {
            return customerRepository.save(model);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void deleteCustomer(Customer customer) {
        try {
            customerRepository.delete(customer);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
