package com.jeretin.ecommerce.user.dto;

import com.jeretin.ecommerce.user.models.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String phone;
    private UserRole userRole;
    private AddressDTO address;
}
