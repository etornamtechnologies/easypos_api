package com.etxtechstack.api.easypos_application.repositories;

import com.etxtechstack.api.easypos_application.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByUsernameIgnoreCase(String username);
    Optional<User> findUserByEmailIgnoreCase(String email);
    Optional<User> findUserByPhone(String phone);
    Optional<User> findUserById(Integer id);

}
