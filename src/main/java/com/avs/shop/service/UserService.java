package com.avs.shop.service;

import com.avs.shop.domain.User;
import com.avs.shop.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {// security

    boolean save(UserDTO userDTO);

    void save(User user);

    void delete(User user);

    List<UserDTO> getAll();

    User findByName(String name);

    void updateProfile(UserDTO userDTO);

    boolean activateUser(String activateCode);

}
