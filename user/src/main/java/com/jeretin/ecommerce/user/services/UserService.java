package com.jeretin.ecommerce.user.services;

import com.jeretin.ecommerce.user.dto.UserDTO;
import com.jeretin.ecommerce.user.dto.UserResponse;
import com.jeretin.ecommerce.user.models.User;

import java.util.Optional;

public interface UserService {

    UserResponse getAllUsers();
    void addNewUser(UserDTO userDTO);
    Optional<UserDTO> getUserById(String id);
    boolean updateUser(String id, User user);
}
