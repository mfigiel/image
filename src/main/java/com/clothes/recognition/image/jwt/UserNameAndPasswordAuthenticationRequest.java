package com.clothes.recognition.image.jwt;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserNameAndPasswordAuthenticationRequest {
    private String username;
    private String password;
}
