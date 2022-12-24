package ru.nir.model.login;

import lombok.Data;

@Data
public class VerifyCodeRequest {
    private String username;
    private String code;
}

