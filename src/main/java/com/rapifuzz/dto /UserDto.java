package com.rapifuzz.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private String username;
    private String email;
    private String phoneNumber;
    private String address;
    private String pinCode;
    private String city;
    private String country;
    private String password;


}
