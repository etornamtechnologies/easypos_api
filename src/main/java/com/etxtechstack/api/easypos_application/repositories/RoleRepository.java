package com.etxtechstack.api.easypos_application.repositories;

import com.etxtechstack.api.easypos_application.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findRoleByNameIgnoreCase(String name);
}
