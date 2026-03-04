package com.jeretin.ecommerce.user.services;

import com.jeretin.ecommerce.user.dto.UserDTO;
import com.jeretin.ecommerce.user.dto.UserResponse;
import com.jeretin.ecommerce.user.models.Address;
import com.jeretin.ecommerce.user.models.User;
import com.jeretin.ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private ModelMapper modelMapper;

    private final UserRepository userRepository;

    @Override
    public UserResponse getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOS = users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .toList();
        UserResponse userResponse = new UserResponse();
        userResponse.setContent(userDTOS);
        return userResponse;
    }

    @Override
    public void addNewUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        User savedUser = userRepository.save(user);
//        return modelMapper.map(savedUser, UserDTO.class);
        /*User user = new User();
        updateUserFromRequest(user, userDTO);
        userRepository.save(user);*/
    }

    @Override
    public Optional<UserDTO> getUserById(String id) {

        return userRepository.findById(id)
                .map(user -> modelMapper.map(user, UserDTO.class));
    }

    @Override
    public boolean updateUser(String id, User user) {
        return userRepository.findById(String.valueOf(id)).map(existingUser -> {
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setEmail(user.getEmail());
            existingUser.setPhone((user.getPhone()));
            existingUser.setRole(user.getRole());
            existingUser.setAddress(user.getAddress());
            userRepository.save(existingUser);
            return true;
        }).orElse(false);
    }

    private void updateUserFromRequest(User user, UserDTO userDTO) {
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        if (userDTO.getAddress() != null) {
            Address address = new Address();
            address.setStreet(userDTO.getAddress().getStreet());
            address.setState(userDTO.getAddress().getState());
            address.setZipcode(userDTO.getAddress().getZipcode());
            address.setCity(userDTO.getAddress().getCity());
            address.setCountry(userDTO.getAddress().getCountry());
            user.setAddress(address);
        }
    }
}
