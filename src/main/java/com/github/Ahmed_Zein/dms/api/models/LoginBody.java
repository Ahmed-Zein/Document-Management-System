package com.github.Ahmed_Zein.dms.api.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginBody {
    @Email
    private String email;

    @NotNull
    @NotBlank
    private String password;
}
