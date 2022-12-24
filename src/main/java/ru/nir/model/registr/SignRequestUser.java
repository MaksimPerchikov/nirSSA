package ru.nir.model.registr;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class SignRequestUser {

    private String name;

    private String username;

    private String email;

    private String password;

    private boolean mfa;
}
