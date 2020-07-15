package com.etxtechstack.api.easypos_application.services;

import com.etxtechstack.api.easypos_application.models.User;
import com.etxtechstack.api.easypos_application.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        try {
            return userRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public User getUserById(Integer userId) {
        try {
            Optional<User> user = userRepository.findUserById(userId);
            if(user.isPresent()) {
                return user.get();
            } else {
                throw new RuntimeException("User Not Found");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public User getUserByUsername(String username) {
        try {
            Optional<User> user = userRepository.findUserByUsernameIgnoreCase(username);
            if(user.isPresent()) {
                return user.get();
            } else {
                throw new RuntimeException("User Not Found");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public User createUser(User model) {
        model.setFailedLoginCount(0);
        model.setIsDefaultPassword("Y");
        model.setStatus("Y");
        try {
            return  userRepository.save(model);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public User updateUser(User model) {
        try {
            return  userRepository.save(model);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void deleleUser(User model) {
        try {
            userRepository.delete(model);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
