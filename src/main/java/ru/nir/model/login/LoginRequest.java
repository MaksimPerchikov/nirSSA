package ru.nir.model.login;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    private String username;

    private String password;
}
