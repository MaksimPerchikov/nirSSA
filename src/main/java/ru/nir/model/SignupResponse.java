package ru.nir.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupResponse {
    private boolean mfa;
    private String secretImageUri;
}

