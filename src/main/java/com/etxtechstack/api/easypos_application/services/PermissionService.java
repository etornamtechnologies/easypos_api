package com.etxtechstack.api.easypos_application.services;

import com.etxtechstack.api.easypos_application.models.Permission;
import com.etxtechstack.api.easypos_application.repositories.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;

    public List<Permission> getAllPermissions() {
        try {
            return permissionRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Permission getPermissionById(Integer permissionId) {
        String notFoundMessage = "Permission With ID /" + permissionId + "/ Not Found";
        try {
            return permissionRepository.findById(permissionId).orElseThrow(()-> {
                return new RuntimeException(notFoundMessage);
            });
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
