package com.rapifuzz.services;


import com.rapifuzz.dto.UserDto;
import com.rapifuzz.entities.User;
import com.rapifuzz.exceptions.BusinessException;
import com.rapifuzz.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User saveUser(UserDto userDto) {
        try{
            validateRequest(userDto);
            String ps = passwordEncoder.encode(userDto.getPassword());
            User user = User.builder().email(userDto.getEmail()).city(userDto.getCity()).country(userDto.getCountry())
                    .address(userDto.getAddress()).email(userDto.getEmail())
                    .password(ps).phoneNumber(userDto.getPhoneNumber())
                    .username(userDto.getUsername()).pinCode(userDto.getPinCode()).build();
            return userRepository.save(user);
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try{
            Optional<User> userDetail = userRepository.findByUsername(username);
            return userDetail;
        }catch (Exception e){
            throw new BusinessException("Something went wrong");
        }
    }

    @Override
    public Optional<User> login(String username, String password) {
        try{
            if (username == null || password == null) {
                throw new IllegalArgumentException("Username and password are required");
            }

            Optional<User> isUserExist = userRepository.findByUsername(username);
            if (isUserExist.isPresent()) {
                User user = isUserExist.get();
                if (passwordEncoder.matches(password,isUserExist.get().getPassword())) {
                    return isUserExist;
                } else {
                    throw new BusinessException("Invalid password");
                }
            } else {
                throw new BusinessException("User not found");
            }
        }catch (IllegalArgumentException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Something went wrong during login");
        }
    }

    private void validateRequest(UserDto userDto){

            if (null == userDto.getUsername()   || userDto.getUsername().isEmpty() || userDto.getUsername().length() < 3 || userDto.getUsername().length() > 50) {
                throw  new BusinessException("Username must be between 3 and 50 characters");
            }

            if (userDto.getEmail() == null || userDto.getEmail().isEmpty() || !isValidEmail(userDto.getEmail())) {
                throw  new BusinessException("Invalid email format");
            }

            if (userDto.getPhoneNumber() == null || userDto.getPhoneNumber().isEmpty() || !userDto.getPhoneNumber().matches("^[0-9]{10}$")) {
                throw  new BusinessException("Phone number should be 10 digits");
            }

            if (userDto.getAddress() == null || userDto.getAddress().isEmpty() || userDto.getUsername().length() < 3 || userDto.getUsername().length() > 25) {
                throw  new BusinessException("Address is required");
            }

            if (userDto.getPinCode() == null || userDto.getPinCode().isEmpty() || !userDto.getPinCode().matches("^[0-9]{5,6}$")) {
                throw  new BusinessException("Pin code should be 5 or 6 digits");
            }

            if (userDto.getCity() == null || userDto.getCity().isEmpty() || userDto.getCity().length() < 4 || userDto.getCity().length() > 25) {
                throw  new BusinessException( "City is required");
            }

            if (userDto.getCountry() == null || userDto.getCountry().isEmpty() || userDto.getCountry().length() < 4 || userDto.getCountry().length() > 25) {
                throw  new BusinessException( "Country is required");
            }

            if ( !isPass(userDto.getPassword())) {

                throw  new BusinessException( "Password is required or length should be greater than 12 and less than 16 and include atleast one special characters e.g. ! & *");
            }

    }//^[!@*]

    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        // Compile the regex pattern for validation
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
        // Match the input email with the regex pattern
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isPass(String password){
        if(null == password || password.isEmpty())
            return false;
        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@*!])[a-zA-Z0-9@*!]{12,16}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

}






