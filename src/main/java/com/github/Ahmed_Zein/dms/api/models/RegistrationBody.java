package com.github.Ahmed_Zein.dms.api.models;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegistrationBody {

    @Email
    @NotNull
    private String email;

    @NotNull
    @NotBlank
    @Size(min = 3)
    private String firstname;

    @NotNull
    @NotBlank
    @Size(min = 3)
    private String lastname;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$", message = "Minimum six characters, at least one letter and one number")
    private String password;
}
