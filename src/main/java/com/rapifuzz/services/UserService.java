package com.rapifuzz.services;


import com.rapifuzz.dto.UserDto;
import com.rapifuzz.entities.User;

import java.util.Optional;

public interface UserService {

    User saveUser(UserDto userDto);

    Optional<User> findByUsername(String username);

    Optional<User> login(String username, String password);
}
