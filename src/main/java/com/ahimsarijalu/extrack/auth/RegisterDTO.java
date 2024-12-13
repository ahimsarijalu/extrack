package com.ahimsarijalu.extrack.auth;

import lombok.Data;

@Data
public class RegisterDTO {
    private String email;
    private String name;
    private String password;
    private Integer age;
}
