package com.example.demo.DTO;

import lombok.Data;

@Data
public class UserAuthControllerDTO {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
