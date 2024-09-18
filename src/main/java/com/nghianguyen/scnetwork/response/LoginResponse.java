package com.nghianguyen.scnetwork.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginResponse {
    private String message;
    private String token;
}
