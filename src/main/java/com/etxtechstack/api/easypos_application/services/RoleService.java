package com.etxtechstack.api.easypos_application.services;

import com.etxtechstack.api.easypos_application.models.Role;
import com.etxtechstack.api.easypos_application.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public List<Role> getAllRoles() {
        try {
            return roleRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Role getRoleById(Integer roleId) {
        try {
            return roleRepository.findById(roleId).orElseThrow(()-> {
                return new RuntimeException("Role With ID /" + roleId + "/ Not Found");
            });
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Role createRole(Role roleModel) {
        try {
            return roleRepository.save(roleModel);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Role updateRole(Role roleModel) {
        try {
            return roleRepository.save(roleModel);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void deleteRole(Role roleModel) {
        try {
            roleRepository.delete(roleModel);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
