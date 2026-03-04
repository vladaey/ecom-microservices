package com.jeretin.ecommerce.user.controllers;

import com.jeretin.ecommerce.user.dto.UserDTO;
import com.jeretin.ecommerce.user.dto.UserResponse;
import com.jeretin.ecommerce.user.models.User;
import com.jeretin.ecommerce.user.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

//    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public UserResponse getAllUsers() {
        System.out.println("REQUEST RECEIVED");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String id) {

        log.info("Request received for user: {}", id);

        log.trace("This is TRACE level - Very detailed logs");
        log.debug("This is DEBUG level - Used for development debugging");
        log.info("This is INFO level - General system information");
        log.warn("This is WARN level - Something might be wrong");
        log.error("This is ERROR level - Something failed");

        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> addNewUser(@RequestBody UserDTO userDTO) {
        userService.addNewUser(userDTO);
        return ResponseEntity.ok("User is added successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable String id, @RequestBody User user) {
        if (userService.updateUser(id, user)) {
            return ResponseEntity.ok("User updated successfully");
        }
        return ResponseEntity.notFound().build();
    }
}
