package com.beatstore.userservice.dto.auth;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

//@Data
@NoArgsConstructor
@Getter
public class RegisterRequestDTO {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;

    public RegisterRequestDTO(String username, String password, String email, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
