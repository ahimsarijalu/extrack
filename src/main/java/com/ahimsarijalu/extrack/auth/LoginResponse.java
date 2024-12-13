package com.ahimsarijalu.extrack.auth;

import com.ahimsarijalu.extrack.user.User;
import com.ahimsarijalu.extrack.user.UserDTO;
import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private UserDTO user;
}
