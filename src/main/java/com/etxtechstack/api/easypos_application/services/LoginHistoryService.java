package com.etxtechstack.api.easypos_application.services;

import com.etxtechstack.api.easypos_application.models.LoginHistory;
import com.etxtechstack.api.easypos_application.repositories.LoginHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginHistoryService {
    @Autowired
    private LoginHistoryRepository loginHistoryRepository;

    public LoginHistory createLoginHistory(LoginHistory model) {
        try {
            return loginHistoryRepository.save(model);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
