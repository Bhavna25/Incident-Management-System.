package com.rapifuzz.controller;

import com.rapifuzz.dto.UserDto;
import com.rapifuzz.entities.User;
import com.rapifuzz.exceptions.BusinessException;
import com.rapifuzz.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser( @RequestBody UserDto userDto){
        if (userService.findByUsername(userDto.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }

        userService.saveUser(userDto);
        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUser(@RequestParam String username){
        Optional<User> user = userService.findByUsername(username);
        if(user.isPresent()){
            return new ResponseEntity<>(user.get(),HttpStatus.FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDto userDto) {
        try {
            Optional<User> authenticUser = userService.login(userDto.getUsername(), userDto.getPassword());
            if (authenticUser.isPresent()) {
                return ResponseEntity.ok("Login successful");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
            }
        }catch (Exception e){
            throw e;
        }
    }

}
