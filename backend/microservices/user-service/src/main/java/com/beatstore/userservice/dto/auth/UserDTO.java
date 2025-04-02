package com.beatstore.userservice.dto.auth;

import com.beatstore.userservice.security.CustomUserDetails;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class UserDTO {
    private String email;
    private String username;
    private String firstName;
    private String lastName;

    public UserDTO(String email, String username, String firstName, String lastName) {
        this.email = email;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UserDTO(CustomUserDetails userDetails) {
        this.email = userDetails.getEmail();
        this.username = userDetails.getUsername();
        this.firstName = userDetails.getFirstName();
        this.lastName = userDetails.getLastName();
    }
}
