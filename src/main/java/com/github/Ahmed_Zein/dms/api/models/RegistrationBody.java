package com.github.Ahmed_Zein.dms.api.models;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationBody {

    @Email
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
