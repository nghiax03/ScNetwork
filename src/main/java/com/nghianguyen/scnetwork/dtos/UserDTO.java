package com.nghianguyen.scnetwork.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nghianguyen.scnetwork.models.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    @NotBlank
    private String username;

    @NotBlank(message = "email cannot be null")
    @Email
    private String email;

    @NotNull(message = "password cannot be null")
    private String password;
    @JsonProperty("full_name")
    private String fullName;

//    private boolean isOnline;

    public User fromUser(){
        return User
                .builder()
                .username(username)
                .email(email)
                .password(password)
                .fullName(fullName)
                .build();
    }
}
